package com.lezhin.clone.backend.entity;

import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.enums.OAuth2Provider;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Member extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String username;

    private String password;

    @Column(nullable = false, columnDefinition = "varchar")
    @Enumerated(EnumType.STRING)
    private MemberType type;

    @Column(nullable = false)
    private String nickname;

    private String phone;

    private String profileImageUrl;

    @ColumnDefault("1")
    @Column(columnDefinition = "TINYINT")
    private boolean status;

    @ColumnDefault("1")
    @Column(columnDefinition = "TINYINT")
    private boolean isFirstPurchase;

    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isOauth2;

    @Column(nullable = false, columnDefinition = "varchar", name = "oauth2_provider")
    @Enumerated(EnumType.STRING)
    private OAuth2Provider oauth2Provider;

    @Column(name = "oauth2_email")
    private String oauth2Email;

    private LocalDateTime lastLoggedinAt;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<LikedComic> likedComis;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Coin> coins;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<MemberEpisode> memberEpisodes;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Gift> gifts;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<SubscribedComic> subscribedComics;

    public void updateLastLoggedinAt() {
        this.lastLoggedinAt = LocalDateTime.now();
    }
}