package com.example.mydiary.exceptionhandler;

import com.example.mydiary.common.CodeEnum;
import com.example.mydiary.exception.CustomException;
import com.example.mydiary.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 컨트롤러에서 throw new CustomException(CodeEnum.UNKOWN_ERROR)으로
 * 에러를 던지면 CustomException으로 실행된 에러가 RestControllerAdvice를 타고
 * handlerCustomException으로 넘어와 아래 로직을 실행 후 결과값을 반환하게 된다
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handlerCustomException(CustomException e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(e.getReturnCode().getCode())
                .body(CommonResponse.builder()
                        .returnCode(e.getReturnCode().getCode())
                        .returnMessage(e.getReturnMessage())
                        .build());
    }
    /**
     * 예외를 아무리 잘 처리해도 예상치 못한 예외가 발생할 수 있다.
     * 그런 경우 예외 처리를 위해 꼭 필요하다.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handlerException(Exception e) {
        log.error("Error occurs {}", e.toString());
        return ResponseEntity.status(CodeEnum.UNKNOWN_ERROR.getCode())
                .body(CommonResponse.builder()
                        .returnCode(CodeEnum.UNKNOWN_ERROR.getCode())
                        .returnMessage(CodeEnum.UNKNOWN_ERROR.getMessage())
                        .build());
    }
}
