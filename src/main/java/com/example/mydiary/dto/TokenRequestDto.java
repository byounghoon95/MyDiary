package com.example.mydiary.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRequestDto {
    String accessToken;
    String refreshToken;
}
