package com.example.mydiary.dto;

import com.example.mydiary.entity.Authority;
import com.example.mydiary.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberJoinRequestDto {
    private String memId;
    private String password;
    private String name;

    public MemberEntity toEntity(Authority authority) {
        return MemberEntity.builder()
                .memId(memId)
                .password(password)
                .authority(authority)
                .name(name)
                .build();
    }
}

/**
 * cannot deserialize from object value 에러 해결
 * ObjectMapper 내부에 기본생성자가 없으면 역직렬화를 막음
 */