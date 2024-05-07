package com.lezhin.clone.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUsername(String username);

    Optional<Member> findByIsOauth2AndOauth2EmailOrUsername(boolean isOauth2, String oauth2Email, String username);
}