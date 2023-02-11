package com.example.mydiary.member.service;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.dto.MemberJoinDto;
import com.example.mydiary.dto.MemberLoginDto;
import com.example.mydiary.entity.MemberEntity;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.member.repository.MemberRepository;
import com.example.mydiary.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    @Value("${jwt.token.secret}")
    private String key;
    @Value("${jwt.token.expired-ms}")
    private Long expireTimeMs;
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
    public String login(MemberLoginDto dto) {
        MemberEntity member = memberRepository.findByMemId(dto.getMemId())
                .orElseThrow(() -> new CustomException(CodeEnum.NOT_FOUND_MEMBER, dto.getMemId() + " 와 일치하는 아이디가 존재하지 않습니다."));
        if (!member.getPassword().equals(dto.getPassword())) {
            throw new CustomException(CodeEnum.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다");
        }

        String token = JwtTokenUtil.createToken(member.getMemId(), key, expireTimeMs);

        return token;
    }
}
