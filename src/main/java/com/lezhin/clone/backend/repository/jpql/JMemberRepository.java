package com.lezhin.clone.backend.repository.jpql;

import static com.lezhin.clone.backend.entity.QCoin.coin;
import static com.lezhin.clone.backend.entity.QMember.member;

import jakarta.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lezhin.clone.backend.enums.CoinType;
import com.querydsl.jpa.impl.JPAQueryFactory;

public interface JMemberRepository {
    public Integer getMemberCoinAmount(Long memberId);
    public Integer getMemberBonusCoinAmount(Long memberId);
    public Integer getMemberPointAmount(Long memberId);
}

@Repository
class JMemberRepositoryImpl implements JMemberRepository {
    
    @Autowired
    private ModelMapper modelMapper;

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JMemberRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    
    @Override
    public Integer getMemberCoinAmount(Long memberId) {
        Integer result = query
            .select(coin.amount.sum())
            .from(member)
            .join(member.coins, coin)
            .where(member.memberId.eq(memberId), coin.type.eq(CoinType.COIN)).fetchOne();
        
        if (result == null) result = 0;

        return result;
    }
    
    @Override
    public Integer getMemberBonusCoinAmount(Long memberId) {
        Integer result = query
            .select(coin.amount.sum())
            .from(member)
            .join(member.coins, coin)
            .where(member.memberId.eq(memberId), coin.type.eq(CoinType.BONUSCOIN)).fetchOne();

        if (result == null) result = 0;

        return result;
    }
    
    @Override
    public Integer getMemberPointAmount(Long memberId) {
        Integer result = query
            .select(coin.amount.sum())
            .from(member)
            .join(member.coins, coin)
            .where(member.memberId.eq(memberId), coin.type.eq(CoinType.POINT)).fetchOne();

        if (result == null) result = 0;

        return result;
    }
}