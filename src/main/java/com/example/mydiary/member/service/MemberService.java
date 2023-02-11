package com.example.mydiary.member.service;

import antlr.Token;
import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.dto.MemberJoinDto;
import com.example.mydiary.dto.MemberLoginDto;
import com.example.mydiary.dto.TokenDto;
import com.example.mydiary.dto.TokenRequestDto;
import com.example.mydiary.entity.MemberEntity;
import com.example.mydiary.entity.RefreshToken;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.member.repository.MemberRepository;
import com.example.mydiary.member.repository.RefreshTokenRepository;
import com.example.mydiary.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Value("${jwt.token.secret}")
    private String key;
    @Value("${jwt.token.expired-ms}")
    private Long expireTimeMs;
    @Value("${jwt.token.refresh-ms}")
    private Long refreshTimeMs;
    @Transactional
    public MemberJoinDto join(MemberJoinDto dto) {
        //memId 중복 체크
        memberRepository.findByMemId(dto.getMemId())
                .ifPresent(member -> {
                    throw new CustomException(CodeEnum.DUPLICATED_MEMBER, dto.getMemId() + " 는 이미 등록된 아이디입니다.");
                });
        memberRepository.save(dto.toEntity());
        return dto;
    }
    public TokenDto login(MemberLoginDto dto) {
        MemberEntity member = memberRepository.findByMemId(dto.getMemId())
                .orElseThrow(() -> new CustomException(CodeEnum.NOT_FOUND_MEMBER, dto.getMemId() + " 와 일치하는 아이디가 존재하지 않습니다."));
        if (!member.getPassword().equals(dto.getPassword())) {
            throw new CustomException(CodeEnum.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다");
        }

        TokenDto token = jwtTokenUtil.createToken(member.getMemId(), key, expireTimeMs, refreshTimeMs);

        return token;
    }

    public TokenDto reissue(@RequestBody TokenRequestDto tokenRequestDto, @RequestBody MemberLoginDto memberLoginDto) {
        //리프레시 토큰 만료 에러
        if (!jwtTokenUtil.validationToken(tokenRequestDto.getRefreshToken(),key, memberLoginDto.getMemId())) {
            throw new CustomException(CodeEnum.EXPIRED_TOKEN, "토큰이 만료되었습니다");
        }

        String accessToken = tokenRequestDto.getAccessToken();
        MemberEntity member = memberRepository.findByMemId(memberLoginDto.getMemId())
                .orElseThrow(() -> new CustomException(CodeEnum.NOT_FOUND_MEMBER, memberLoginDto.getMemId() + " 와 일치하는 아이디가 존재하지 않습니다."));
        RefreshToken refreshToken = refreshTokenRepository.findByKey(member.getId())
                .orElseThrow(() -> new CustomException(CodeEnum.NOT_FOUND_REFRESH_TOKEN, "리프레시 토큰값을 조회할 수 없습니다."));

        //리프레시토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())) {
            throw new CustomException(CodeEnum.NOT_FOUND_REFRESH_TOKEN, "리프레시 토큰값을 조회할 수 없습니다.");
        }

        TokenDto newToken = jwtTokenUtil.createToken(member.getMemId(), key, expireTimeMs, refreshTimeMs);
        RefreshToken updatedRefreshToken = refreshToken.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(updatedRefreshToken);

        return newToken;
    }

}
