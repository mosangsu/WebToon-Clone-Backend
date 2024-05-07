package com.lezhin.clone.backend.entity;

import com.lezhin.clone.backend.enums.MemberType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private MemberType type;

    @Column(nullable = false)
    private String nickname;

    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isOauth2;

    @Column(name = "oauth2_provider")
    private String oauth2Provider;

    @Column(name = "oauth2_email")
    private String oauth2Email;

    private LocalDateTime lastLoggedinAt;

    public void updateLastLoggedinAt() {
        this.lastLoggedinAt = LocalDateTime.now();
    }
}