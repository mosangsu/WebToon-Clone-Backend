package com.lezhin.clone.backend.repository.jpql;

import static com.lezhin.clone.backend.entity.QCoin.coin;
import static com.lezhin.clone.backend.entity.QCoinDetail.coinDetail;
import static com.lezhin.clone.backend.entity.QCoinPackage.coinPackage;

import jakarta.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.lezhin.clone.backend.dto.PaymentDto;
import com.lezhin.clone.backend.enums.CoinType;
import com.lezhin.clone.backend.enums.PackageType;

import java.util.List;
import java.util.stream.Collectors;

public interface JCoinPackageRepository {
    public List<PaymentDto.List.Res> getCoinPackages(Long memberId, boolean isFirstPurchase);
}

@Repository
class JCoinPackageRepositoryImpl implements JCoinPackageRepository {
    
    @Autowired
    private ModelMapper modelMapper;

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JCoinPackageRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<PaymentDto.List.Res> getCoinPackages(Long memberId, boolean isFirstPurchase) {
        
        List<PaymentDto.CoinDetails.Res> pointDetails = query
            .select(
                Projections.bean(PaymentDto.CoinDetails.Res.class,
                coin.coinId.as("coinId"),
                coinDetail.coinAccumId.as("coinAccumId"),
                coinDetail.amount.sum().as("amount")))
            .from(coinDetail)
            .innerJoin(coinDetail.coin, coin)
            .where(coin.member.memberId.eq(memberId), coin.type.eq(CoinType.POINT))
            .groupBy(coinDetail.coinAccumId)
            .having(coinDetail.amount.sum().gt(0))
            .orderBy(coin.expirationDate.asc())
            .fetch();
        
        Integer amount = pointDetails.stream().map(pd -> pd.getAmount()).reduce(0, Integer::sum);

        List<PaymentDto.List.Res> result = query
            .select(coinPackage)
            .from(coinPackage)
            .where(checkFirstPayment(isFirstPurchase))
            .fetch().stream().map(c -> modelMapper.map(c, PaymentDto.List.Res.class)).collect(Collectors.toList());
        result = result.stream().map(r -> {r.setPurchasable(r.getPointPrice() <= amount); return r;}).toList();
            
        return result;
    }
    /**
     * 첫결제 회원이면 첫결제 상품도 보여주고 그렇지 않으면 제외
     * @param isFirstPurchase
     * @return Predicate
     */
    private Predicate checkFirstPayment(boolean isFirstPurchase) {
        if (isFirstPurchase) return null;
        return coinPackage.type.ne(PackageType.FIRST_PURCHASE);
    }
}