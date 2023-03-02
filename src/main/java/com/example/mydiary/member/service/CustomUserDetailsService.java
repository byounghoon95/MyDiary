package com.example.mydiary.member.service;


import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.entity.Authority;
import com.example.mydiary.entity.MemberEntity;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String memId)  {
        log.debug("CustomUserDetailsService -> email = {}",memId);
        return memberRepository.findByMemId(memId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new CustomException(CodeEnum.NOT_FOUND_MEMBER,memId + " 와 일치하는 아이디가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public MemberEntity getMember(String memId) {
        return memberRepository.findByMemId(memId)
                .orElseThrow(()->new CustomException(CodeEnum.NOT_FOUND_MEMBER,memId + " 와 일치하는 아이디가 존재하지 않습니다."));
    }

    // DB 에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(MemberEntity member) {


        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(member.getAuthority().getAuthorityName());
        log.debug("authority : {}",authority);

        return new User(
                member.getMemId(),
                member.getPassword(),
                Collections.singleton(authority)
//              authList
        );
    }
}