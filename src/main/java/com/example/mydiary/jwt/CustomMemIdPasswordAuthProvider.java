package com.example.mydiary.jwt;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomMemIdPasswordAuthProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    protected void additionalAuthenticationChecks(UserDetails userDetails, CustomMemIdPasswordAuthToken authentication) {

        log.debug("additionalAuthenticationChecks authentication = {}",authentication);

        if (authentication.getCredentials() == null) {
            log.debug("additionalAuthenticationChecks is null !");
            throw new CustomException(CodeEnum.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다");
        }
        String presentedPassword = authentication.getCredentials().toString();
        log.debug("authentication.presentedPassword = {}",presentedPassword);

        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            throw new CustomException(CodeEnum.INVALID_PASSWORD, "패스워드를 잘못 입력했습니다");
        }
    }

    @Override
    public Authentication authenticate(Authentication authentication)  {
        UserDetails user = null;
        try {
            user = retrieveUser(authentication.getName());
        }catch (CustomException e) {
            throw new CustomException();
        }

        Object principalToReturn = user;
        CustomMemIdPasswordAuthToken result = new CustomMemIdPasswordAuthToken(principalToReturn
                ,authentication.getCredentials()
                ,this.authoritiesMapper.mapAuthorities(user.getAuthorities()));
        additionalAuthenticationChecks(user,result);
        result.setDetails(authentication.getDetails());
        return result;
    }

    protected final UserDetails retrieveUser(String username ) {
        try {
            UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);
            if (loadedUser == null) {
                throw new CustomException(CodeEnum.UNKNOWN_ERROR, "내부 인증 로직중 알수 없는 오류가 발생하였습니다.");
            }
            return loadedUser;
        }
        catch (Exception ex) {
            throw new CustomException(CodeEnum.UNKNOWN_ERROR, "내부 인증 로직중 알수 없는 오류가 발생하였습니다.");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(CustomMemIdPasswordAuthToken.class);
    }
}