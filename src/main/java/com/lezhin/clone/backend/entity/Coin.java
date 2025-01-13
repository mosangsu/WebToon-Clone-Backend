package com.lezhin.clone.backend.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.DynamicInsert;

import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@DynamicInsert
@NoArgsConstructor
public class Coin extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Convert(converter = CoinStatus.CoinStatusAttributeConverter.class)
    private CoinStatus status;

    @Convert(converter = CoinType.CoinTypeAttributeConverter.class)
    private CoinType type;

    @Column(nullable = false)
    private int amount;
    
    @Column(nullable = false)
    private String description;
    
    private LocalDateTime expirationDate;

    @OneToMany(mappedBy = "coin", cascade = CascadeType.PERSIST)
    private List<CoinDetail> coinDetails;

    public Coin (Member member, CoinStatus status, CoinType type, int amount, String description) {
        this.member = member;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public Coin (Member member, CoinStatus status, CoinType type, int amount, String description, LocalDateTime expirationDate) {
        this.member = member;
        this.status = status;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.expirationDate = expirationDate;
    }

}