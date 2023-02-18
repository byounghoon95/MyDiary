package com.example.mydiary.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CodeEnum {
    SUCCESS(HttpStatus.OK,"SUCCESS"),
    DUPLICATED_MEMBER(HttpStatus.CONFLICT,"Member id is duplicated"),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND,"Member id is not existed"),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND,"RefreshToken is not existed"),
    NOT_FOUND_AUTHORITY(HttpStatus.UNAUTHORIZED, "No Authority"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED,"Password is incorrect"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"Token is expired"),
    LOGOUT_MEMBER(HttpStatus.NOT_FOUND,"Member is logout"),
    BAD_TOKEN(HttpStatus.UNAUTHORIZED,"Bad Token"),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"UNKNOWN_ERROR");

    private HttpStatus code;
    private String message;

}
