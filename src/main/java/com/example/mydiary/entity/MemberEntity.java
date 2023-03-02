package com.example.mydiary.entity;

import com.example.mydiary.dto.MemberUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member")
@Entity
public class MemberEntity {
    @Id
    @Column(unique = true, nullable = false, name = "member_id")
    private String memId;
    @NotNull
    private String password;
    @NotNull
    private String name;

    @OneToOne
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name="member_id",referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name",referencedColumnName = "authority_name")}
    )
    private Authority authority;

    public static MemberEntity of(String memId, String password, String name) {
        return new MemberEntity(memId, password, name);
    }
    public MemberEntity(String memId, String password, String name) {
        this.memId = memId;
        this.password = password;
        this.name = name;
    }
}
