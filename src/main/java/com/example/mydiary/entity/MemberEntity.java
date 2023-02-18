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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, name = "member_id")
    private String memId;
    @NotNull
    private String password;
    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(
            name = "member_authority",
            joinColumns = {@JoinColumn(name="member_id",referencedColumnName = "member_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name",referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities = new HashSet<>();
    public void addAuthority(Authority authority) {
        this.getAuthorities().add(authority);
    }
    public void removeAuthority(Authority authority) {
        this.getAuthorities().remove(authority);
    }
    public String getAuthoritiesToString() {
        return this.authorities.stream()
                .map(Authority::getAuthorityName)
                .collect(Collectors.joining(","));
    }

    public void updateMember(MemberUpdateDTO dto, PasswordEncoder passwordEncoder) {
        if(dto.getPassword() != null) this.password = passwordEncoder.encode(dto.getPassword());
        if(dto.getAuthorities().size() > 0) {
            this.authorities = dto.getAuthorities().stream()
                    .filter(MemberAuth::containsKey)
                    .map(MemberAuth::get)
                    .map(Authority::new)
                    .collect(Collectors.toSet());
        }
    }
    public static MemberEntity of(String memId, String password, String name) {
        return new MemberEntity(memId, password, name);
    }
    public MemberEntity(String memId, String password, String name) {
        this.memId = memId;
        this.password = password;
        this.name = name;
    }
}
