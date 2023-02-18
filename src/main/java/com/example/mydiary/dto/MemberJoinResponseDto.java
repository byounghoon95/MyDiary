package com.example.mydiary.dto;

import com.example.mydiary.entity.MemberEntity;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class MemberJoinResponseDto {
    private String memId;
    private String name;
}
