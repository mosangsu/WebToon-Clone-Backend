package com.lezhin.clone.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lezhin.clone.backend.enums.GiftStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Gift extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long giftId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Convert(converter = GiftStatus.GiftStatusAttributeConverter.class)
    private GiftStatus status;

    @Column(nullable = false)
    private int coins;
    @Column(nullable = false)
    private int bonusCoins;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private String imageUrl;
    private LocalDateTime expirationDate;

    public Gift (Member member, GiftStatus status, int coins, int bonusCoins, int points, String title, String description, String imageUrl, LocalDateTime expirationDate) {
        this.member = member;
        this.status = status;
        this.coins = coins;
        this.bonusCoins = bonusCoins;
        this.points = points;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.expirationDate = expirationDate;
    }
}