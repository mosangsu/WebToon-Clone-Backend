// package com.lezhin.clone.backend.repository.jpql;

// import jakarta.persistence.EntityManager;

// import static com.lezhin.clone.backend.entity.QBonusCoin.bonusCoin;
// import static com.lezhin.clone.backend.entity.QBonusCoinDetail.bonusCoinDetail;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.support.PageableExecutionUtils;
// import org.springframework.stereotype.Repository;

// import com.lezhin.clone.backend.dto.MemberDto;
// import com.lezhin.clone.backend.entity.BonusCoin;
// import com.querydsl.jpa.JPQLQuery;
// import com.querydsl.jpa.impl.JPAQuery;
// import com.querydsl.jpa.impl.JPAQueryFactory;

// public interface JBonusCoinRepository {
//     public Page<MemberDto.ExpiringCoins.Res.BonusCoinDto> getExpiringBonusCoins(Long memberId, Pageable pageable);
// }

// @Repository
// class JBonusCoinRepositoryImpl implements JBonusCoinRepository {
    
//     @Autowired
//     private ModelMapper modelMapper;

//     private final EntityManager em;
//     private final JPAQueryFactory query;

//     public JBonusCoinRepositoryImpl(EntityManager em) {
//         this.em = em;
//         this.query = new JPAQueryFactory(em);
//     }

    // @Override
    // public Page<MemberDto.ExpiringCoins.Res.BonusCoinDto> getExpiringBonusCoins(Long memberId, Pageable pageable) {
        
    //     List<MemberDto.ExpiringCoins.Res.BonusCoinDto> result = query
    //         .select(bonusCoin)
    //         .from(bonusCoin)
    //         .join(bonusCoin.bonusCoinDetails, bonusCoinDetail)
    //         .where(bonusCoin.member.memberId.eq(memberId))
    //         .groupBy(bonusCoinDetail.bonusCoinAccumId)
    //         .having(bonusCoinDetail.amount.sum().gt(0))
    //         .orderBy(bonusCoin.expirationDate.asc())
    //         .offset(pageable.getOffset())
    //         .limit(pageable.getPageSize())
    //         .fetch().stream().map(c -> modelMapper.map(c, MemberDto.ExpiringCoins.Res.BonusCoinDto.class)).collect(Collectors.toList());
        
    //     JPAQuery<BonusCoin> countQuery = query
    //         .select(bonusCoin)
    //         .from(bonusCoin)
    //         .join(bonusCoin.bonusCoinDetails, bonusCoinDetail)
    //         .where(bonusCoin.member.memberId.eq(memberId))
    //         .groupBy(bonusCoinDetail.bonusCoinAccumId)
    //         .having(bonusCoinDetail.amount.sum().gt(0));
    //     Long totalCount = getTotalCountGroupBy(pageable, countQuery);
        
    //     return new PageImpl<>(result, pageable, totalCount);
    // }

//     private <T> Long getTotalCountGroupBy(Pageable pageable, JPQLQuery<T> query) {
//         List<T> queryResult = query.fetch();
//         int totalCount = queryResult.size();

//         long offset = pageable.getOffset();

//         // totalCount 보다 큰 값이 들어온 경우
//         if (offset > totalCount) {
//             return Integer.valueOf(totalCount).longValue();
//         }

//         return Integer.valueOf(totalCount).longValue();
//     }
// }