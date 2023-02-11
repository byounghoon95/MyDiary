package com.example.mydiary.utils;

import antlr.Token;
import com.example.mydiary.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
@Slf4j
@Component
public class JwtTokenUtil {
    public TokenDto createToken(String memId, String key, long expireTimeMs, long refreshTimeMs) {
        Claims claims = Jwts.claims();
        claims.put("memId", memId);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() + refreshTimeMs))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();

        return TokenDto.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireTime(expireTimeMs)
                .build();
    }

    public boolean validationToken(String token, String key, String memId) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error(e.toString());
            return false;
        }
    }
}