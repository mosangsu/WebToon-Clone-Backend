package com.lezhin.clone.backend.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lezhin.clone.backend.entity.CoinDetail;
import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.MemberType;
import com.lezhin.clone.backend.enums.OAuth2Provider;
import com.lezhin.clone.backend.enums.PackageType;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class PaymentDto {
    public static class List {

        @Getter
        @Setter
        public static class Res {
            private Long coinPackageId;
            private int coins;
            private int bonusCoins;
            private int points;
            private int price;
            private int originalPrice;
            private int pointPrice;
            private String description;
            private String imageUrl;
            private PackageType type;
            @JsonProperty("isPurchasable")
            private boolean isPurchasable;
        }
    }

    public static class Purchase {
        @Getter
        @Setter
        public static class Req {
            private Long coinPackageId;
        }
    }

    public static class CoinDetails {
        @Getter
        @Setter
        @NoArgsConstructor
        public static class Res {
            private Long coinId;
            private Long coinAccumId;
            private Integer amount;
        }
    }
}