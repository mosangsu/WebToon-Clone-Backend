package com.lezhin.clone.backend.repository.jpql;

import jakarta.persistence.EntityManager;

import static com.lezhin.clone.backend.entity.QCoin.coin;
import static com.lezhin.clone.backend.entity.QCoinDetail.coinDetail;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.lezhin.clone.backend.dto.CoinDto;
import com.lezhin.clone.backend.dto.MemberDto;
import com.lezhin.clone.backend.dto.PaymentDto;
import com.lezhin.clone.backend.entity.Coin;
import com.lezhin.clone.backend.enums.CoinStatus;
import com.lezhin.clone.backend.enums.CoinType;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface JCoinRepository {
    public List<CoinDto.CoinDetails.Res> getCoinDetails(Long memberId, CoinType type);
    public Page<MemberDto.ExpiringCoins.Res> getExpiringCoins(Long memberId, CoinType type, Pageable pageable);
    public Page<MemberDto.ChargeHistory.Res> getChargeHistory(Long memberId, Pageable pageable);
    public Page<MemberDto.UsageHistory.Res> getUsageHistory(Long memberId, CoinType type, Pageable pageable);
}

@Repository
class JCoinRepositoryImpl implements JCoinRepository {
    
    @Autowired
    private ModelMapper modelMapper;

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JCoinRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    @Override
    public List<CoinDto.CoinDetails.Res> getCoinDetails(Long memberId, CoinType type) {
        
        List<CoinDto.CoinDetails.Res> result = query
            .select(
                Projections.bean(CoinDto.CoinDetails.Res.class,
                coin.coinId.as("coinId"),
                coinDetail.coinAccumId.as("coinAccumId"),
                coinDetail.amount.sum().as("amount")))
            .from(coinDetail)
            .innerJoin(coinDetail.coin, coin)
            .where(coin.member.memberId.eq(memberId), coin.type.eq(type))
            .groupBy(coinDetail.coinAccumId)
            .having(coinDetail.amount.sum().gt(0))
            .orderBy(coin.expirationDate.asc())
            .fetch();
        
        return result;
    }

    @Override
    public Page<MemberDto.ExpiringCoins.Res> getExpiringCoins(Long memberId, CoinType type, Pageable pageable) {
        List<MemberDto.ExpiringCoins.Res> result = query
            .select(Projections.bean(MemberDto.ExpiringCoins.Res.class,
                coin.coinId,
                coin.status,
                coinDetail.amount.sum().as("amount"),
                coin.description,
                coin.expirationDate,
                coin.type))
            .from(coin)
            .join(coin.coinDetails, coinDetail)
            .where(coin.member.memberId.eq(memberId), typeEq(type))
            .groupBy(coinDetail.coinAccumId)
            .having(coinDetail.amount.sum().gt(0))
            .orderBy(coin.expirationDate.asc().nullsLast())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
        
        JPAQuery<Coin> countQuery = query
            .select(coin)
            .from(coin)
            .join(coin.coinDetails, coinDetail)
            .where(coin.member.memberId.eq(memberId), typeEq(type))
            .groupBy(coinDetail.coinAccumId)
            .having(coinDetail.amount.sum().gt(0));
        Long totalCount = getTotalCountGroupBy(pageable, countQuery);
        
        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<MemberDto.ChargeHistory.Res> getChargeHistory(Long memberId, Pageable pageable) {
        List<MemberDto.ChargeHistory.Res> result = query
            .select(coin)
            .from(coin)
            .where(coin.member.memberId.eq(memberId), coin.status.in(CoinStatus.ACCUMULATE, CoinStatus.EXPIRE))
            .orderBy(coin.createdAt.desc().nullsLast())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch().stream().map(c -> modelMapper.map(c, MemberDto.ChargeHistory.Res.class)).collect(Collectors.toList());
        
        JPAQuery<Long> countQuery = query
            .select(coin.count())
            .from(coin)
            .where(coin.member.memberId.eq(memberId))
            .distinct();
        
        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<MemberDto.UsageHistory.Res> getUsageHistory(Long memberId, CoinType type, Pageable pageable) {
        List<MemberDto.UsageHistory.Res> result = query
            .select(coin)
            .from(coin)
            .where(coin.member.memberId.eq(memberId), coin.status.eq(CoinStatus.USE), typeEq(type))
            .orderBy(coin.createdAt.desc().nullsLast())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch().stream().map(c -> modelMapper.map(c, MemberDto.UsageHistory.Res.class)).collect(Collectors.toList());
        
        JPAQuery<Long> countQuery = query
            .select(coin.count())
            .from(coin)
            .where(coin.member.memberId.eq(memberId))
            .distinct();
        
        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private Predicate typeEq(CoinType type) {
        if (type == null || type.equals(CoinType.ALL)) return null;
        return coin.type.eq(type);
    }

    private <T> Long getTotalCountGroupBy(Pageable pageable, JPQLQuery<T> query) {
        List<T> queryResult = query.fetch();
        int totalCount = queryResult.size();

        long offset = pageable.getOffset();

        // totalCount 보다 큰 값이 들어온 경우
        if (offset > totalCount) {
            return Integer.valueOf(totalCount).longValue();
        }

        return Integer.valueOf(totalCount).longValue();
    }
}