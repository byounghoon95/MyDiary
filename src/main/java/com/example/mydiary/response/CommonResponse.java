package com.example.mydiary.response;

import com.example.mydiary.common.CodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
@Builder
/* NON_NULL을 사용하면 세 변수 중 들어오지 않은 변수는 노출하지 않음 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    private HttpStatus returnCode;
    private String returnMessage;
    private T data;
    
    /* 응답의 방식이 다른 경우를 위해 메서드 오버로딩 */
    public CommonResponse(CodeEnum codeEnum){
        setReturnCode(codeEnum.getCode());
        setReturnMessage(codeEnum.getMessage());
    }

    /* 데이터가 전달되는 경우는 무조건 응답 성공 */
    public CommonResponse(T data) {
        setReturnCode(CodeEnum.SUCCESS.getCode());
        setReturnMessage(CodeEnum.SUCCESS.getMessage());
        setData(data);
    }

    public CommonResponse(CodeEnum codeEnum, T data) {
        setReturnCode(codeEnum.getCode());
        setReturnMessage(codeEnum.getMessage());
        setData(data);
    }
}
