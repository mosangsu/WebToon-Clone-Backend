package com.lezhin.clone.backend.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CoinDetail extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coinId")
    private Coin coin;

    @Convert(converter = CoinStatus.CoinStatusAttributeConverter.class)
    private CoinStatus status;

    @Convert(converter = CoinType.CoinTypeAttributeConverter.class)
    private CoinType type;

    @Column(nullable = false)
    private int amount;

    private Long coinAccumId;
    
    private LocalDateTime expirationDate;

    public CoinDetail (Coin coin, CoinStatus status, CoinType type, int amount) {
        this.coin = coin;
        this.status = status;
        this.type = type;
        this.amount = amount;
    }

    public CoinDetail (Coin coin, CoinStatus status, CoinType type, int amount, LocalDateTime expirationDate) {
        this.coin = coin;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.expirationDate = expirationDate;
    }
}