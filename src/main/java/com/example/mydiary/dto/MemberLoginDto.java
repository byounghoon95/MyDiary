package com.example.mydiary.dto;


import com.example.diary.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginDto {
    private String memId;
    private String password;

    public MemberEntity toEntity(MemberLoginDto dto) {
        return MemberEntity.builder()
                .memId(dto.memId)
                .password(dto.password)
                .build();
    }
}
