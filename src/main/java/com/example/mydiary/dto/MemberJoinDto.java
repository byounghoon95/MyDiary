package com.example.mydiary.dto;

import com.example.diary.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@AllArgsConstructor
@Setter
@Getter
public class MemberJoinDto {
    private String memId;
    private String password;
    private String name;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .memId(memId)
                .name(password)
                .password(name)
                .build();
    }
}
