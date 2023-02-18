package com.example.mydiary.member.repository;

import com.example.mydiary.entity.Authority;
import com.example.mydiary.entity.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority,String> {
    Optional<Authority> findByAuthorityName(MemberAuth authorityName);
}