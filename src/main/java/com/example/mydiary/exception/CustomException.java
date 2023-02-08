package com.example.mydiary.exception;

import com.example.mydiary.common.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomException extends RuntimeException {
    private CodeEnum returnCode;
    private String returnMessage;
}
