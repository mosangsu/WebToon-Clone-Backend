package com.lezhin.clone.backend.entity;

import java.util.List;

import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.PackageType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CoinPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinPackageId;

    @Column(nullable = false)
    private int coins;
    @Column(nullable = false)
    private int bonusCoins;
    @Column(nullable = false)
    private int points;
    @Column(nullable = false)
    private int price;
    @Column(nullable = false)
    private int originalPrice;
    private Integer pointPrice;
    @Column(nullable = false)
    private String description;
    private String imageUrl;
    @Convert(converter = PackageType.PackageTypeAttributeConverter.class)
    private PackageType type;
}