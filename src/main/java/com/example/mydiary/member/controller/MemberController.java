package com.example.mydiary.member.controller;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.dto.MemberJoinDto;
import com.example.mydiary.dto.MemberLoginDto;
import com.example.mydiary.member.service.MemberService;
import com.example.mydiary.response.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(tags = {"로그인 및 회원가입 API"})
@RequiredArgsConstructor
@Getter
@RequestMapping("/api/users")
@RestController
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "회원가입", notes = "회원의 정보를 입력받아 등록한다")
    @PostMapping("/join")
    public CommonResponse join(@RequestBody MemberJoinDto dto) {
        return new CommonResponse(CodeEnum.SUCCESS, memberService.join(dto));
    }

    @ApiOperation(value = "로그인", notes = "회원의 정보로 로그인하면 JWT를 발급한다")
    @PostMapping("/login")
    public CommonResponse login(@RequestBody MemberLoginDto dto) {
        log.info("로그인 계정 : " + dto);
        String token = memberService.login(dto);
        return new CommonResponse(CodeEnum.SUCCESS, token);
    }
}
