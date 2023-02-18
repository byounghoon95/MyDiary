package com.example.mydiary.member.controller;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.dto.MemberJoinRequestDto;
import com.example.mydiary.dto.MemberLoginDto;
import com.example.mydiary.dto.TokenDto;
import com.example.mydiary.dto.TokenRequestDto;
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
    public CommonResponse join(@RequestBody MemberJoinRequestDto dto) {
        return new CommonResponse(CodeEnum.SUCCESS, memberService.join(dto));
    }

    @ApiOperation(value = "로그인", notes = "회원의 정보로 로그인하면 JWT를 발급한다")
    @PostMapping("/login")
    public CommonResponse login(@RequestBody MemberLoginDto dto) {
        return new CommonResponse(CodeEnum.SUCCESS, memberService.login(dto));
    }

    @ApiOperation(value = "엑세스,리프레시 토큰 재발급", notes = "access 토큰 만료 시, 회원 검증 후 리프레시 토큰을 검증해서 엑세스 토큰과 리프레시 토큰 재발급")
    @PostMapping("/reissue")
    public CommonResponse jwtReissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return new CommonResponse(CodeEnum.SUCCESS, memberService.reissue(tokenRequestDto));
    }

}