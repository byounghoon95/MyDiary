package com.example.mydiary.member.service;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.dto.*;
import com.example.mydiary.entity.Authority;
import com.example.mydiary.entity.MemberAuth;
import com.example.mydiary.entity.MemberEntity;
import com.example.mydiary.entity.RefreshToken;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.jwt.CustomMemIdPasswordAuthToken;
import com.example.mydiary.member.repository.AuthorityRepository;
import com.example.mydiary.member.repository.MemberRepository;
import com.example.mydiary.member.repository.RefreshTokenRepository;
import com.example.mydiary.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthorityRepository authorityRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    @Transactional
    public MemberJoinResponseDto join(MemberJoinRequestDto requestDto) {
        //memId 중복 체크
        memberRepository.findByMemId(requestDto.getMemId())
                .ifPresent(member -> {
                    throw new CustomException(CodeEnum.DUPLICATED_MEMBER, requestDto.getMemId() + " 는 이미 등록된 아이디입니다.");
                });
        // DB 에서 ROLE_USER를 찾아서 권한으로 추가
        Authority authority = authorityRepository
                .findByAuthorityName(MemberAuth.ROLE_USER).orElseThrow(()->new CustomException(CodeEnum.NOT_FOUND_AUTHORITY,"권한이 없습니다"));


        requestDto.setPassword(encoder.encode(requestDto.getPassword()));
        memberRepository.save(requestDto.toEntity(authority));

        return new MemberJoinResponseDto(requestDto.getMemId(),requestDto.getName());
    }
    public TokenDto login(MemberLoginDto dto) {
        CustomMemIdPasswordAuthToken customMemIdPasswordAuthToken  = new CustomMemIdPasswordAuthToken(dto.getMemId(),dto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(customMemIdPasswordAuthToken);

        String memId = authenticate.getName();
        MemberEntity member = customUserDetailsService.getMember(memId);

        String accessToken = tokenProvider.createAccessToken(memId, member.getAuthority());
        String refreshToken = tokenProvider.createRefreshToken(memId, member.getAuthority());

        //refresh Token 저장
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .key(memId)
                        .value(refreshToken)
                        .build()
        );

        return tokenProvider.createTokenDto(accessToken,refreshToken);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        /*
         *  accessToken 은 JWT Filter 에서 검증되고 옴
         * */
        String originAccessToken = tokenRequestDto.getAccessToken();
        String originRefreshToken = tokenRequestDto.getRefreshToken();

        // refreshToken 검증
        boolean refreshTokenFlag = tokenProvider.validateToken(originRefreshToken);

        log.debug("refreshTokenFlag = {}", refreshTokenFlag);

        //refreshToken 검증하고 상황에 맞는 오류를 내보낸다.
        if (!refreshTokenFlag) {
            throw new CustomException(CodeEnum.EXPIRED_TOKEN,"토큰의 유효기간이 끝났습니다");
        }

        // 2. Access Token 에서 Member Email 가져오기
        Authentication authentication = tokenProvider.getAuthentication(originAccessToken);

        log.debug("Authentication = {}",authentication);
        log.debug("Authentication.getName = {}",authentication.getName());


        // 3. 저장소에서 memId 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new CustomException(CodeEnum.LOGOUT_MEMBER,"사용자가 로그아웃하였습니다.")); // 로그 아웃된 사용자


        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(originRefreshToken)) {
            throw new CustomException(CodeEnum.BAD_TOKEN,"토큰이 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        String memId = tokenProvider.getMemIdByToken(originAccessToken);
        MemberEntity member = customUserDetailsService.getMember(memId);

        String newAccessToken = tokenProvider.createAccessToken(memId, member.getAuthority());
        String newRefreshToken = tokenProvider.createRefreshToken(memId, member.getAuthority());
        TokenDto tokenDto = tokenProvider.createTokenDto(newAccessToken, newRefreshToken);

        log.debug("refresh Origin = {}",originRefreshToken);
        log.debug("refresh New = {} ",newRefreshToken);

        // 6. 저장소 정보 업데이트
        refreshToken.updateValue(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }

}
