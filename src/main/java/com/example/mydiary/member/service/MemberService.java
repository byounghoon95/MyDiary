package com.example.mydiary.member.service;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.dto.MemberJoinDto;
import com.example.mydiary.dto.MemberLoginDto;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    @Transactional
    public MemberJoinDto join(MemberJoinDto dto) {
        //memId 중복 체크
        memberRepository.findByMemId(dto.getMemId())
                .ifPresent(member -> {
                    throw new CustomException(CodeEnum.DUPLICATED_MEMBER, dto.getMemId() + " 는 이미 등록된 아이디입니다.");
                });
//        dto.setPassword(encoder.encode(dto.getPassword()));
        memberRepository.save(dto.toEntity());
        return dto;
    }
    public String login(MemberLoginDto dto) {
        return "token";
    }
}
