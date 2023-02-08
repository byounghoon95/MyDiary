package com.example.mydiary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Member")
@Entity
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String memId;
    @NotNull
    private String password;
    @NotNull
    private String name;

    public MemberEntity(String memId, String password, String name) {
        this.memId = memId;
        this.password = password;
        this.name = name;
    }

    public static MemberEntity of(String memId, String password, String name) {
        MemberEntity memberEntity = new MemberEntity(memId, password, name);
        return memberEntity;
    }
}
