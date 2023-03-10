package com.example.mydiary.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "refresh_token")
@Entity
public class RefreshToken {
    @Id
    @Column(name = "rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;
    public void updateValue(String token) {
        this.value = token;
    }
    @Builder
    public RefreshToken(String key, String value) {
        this.key = key;
        this.value = value;
    }
}