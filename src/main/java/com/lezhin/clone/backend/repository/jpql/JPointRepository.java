// package com.lezhin.clone.backend.repository.jpql;

// import static com.lezhin.clone.backend.entity.QPoint.point;
// import static com.lezhin.clone.backend.entity.QPointDetail.pointDetail;

// import java.util.ArrayList;
// import java.util.List;
// import java.util.stream.Collectors;

// import jakarta.persistence.EntityManager;

// import org.hibernate.query.criteria.JpaExpression;
// import org.modelmapper.ModelMapper;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageImpl;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.support.PageableExecutionUtils;
// import org.springframework.stereotype.Repository;

// import com.lezhin.clone.backend.dto.MemberDto;
// import com.lezhin.clone.backend.dto.PaymentDto;
// import com.lezhin.clone.backend.entity.Point;
// import com.querydsl.core.types.Projections;
// import com.querydsl.core.types.dsl.Expressions;
// import com.querydsl.jpa.JPAExpressions;
// import com.querydsl.jpa.JPQLQuery;
// import com.querydsl.jpa.impl.JPAQuery;
// import com.querydsl.jpa.impl.JPAQueryFactory;

// public interface JPointRepository {
//     public List<PaymentDto.PointDetails.Res> getPointDetails(Long memberId);
//     public Page<MemberDto.ExpiringCoins.Res.PointDto> getExpiringPoints(Long memberId, Pageable pageable);
// }

// @Repository
// class JPointRepositoryImpl implements JPointRepository {
    
//     @Autowired
//     private ModelMapper modelMapper;

//     private final EntityManager em;
//     private final JPAQueryFactory query;

//     public JPointRepositoryImpl(EntityManager em) {
//         this.em = em;
//         this.query = new JPAQueryFactory(em);
//     }

//     @Override
//     public List<PaymentDto.PointDetails.Res> getPointDetails(Long memberId) {
        
//         List<PaymentDto.PointDetails.Res> result = query
//             .select(
//                 Projections.bean(PaymentDto.PointDetails.Res.class,
//                 point.pointId.as("pointId"),
//                 pointDetail.pointAccumId.as("pointAccumId"),
//                 pointDetail.amount.sum().as("amount")))
//             .from(pointDetail)
//             .innerJoin(pointDetail.point, point)
//             .where(point.member.memberId.eq(memberId))
//             .groupBy(pointDetail.pointAccumId)
//             .having(pointDetail.amount.sum().gt(0))
//             .orderBy(point.expirationDate.asc())
//             .fetch();
        
//         return result;
//     }

//     @Override
//     public Page<MemberDto.ExpiringCoins.Res.PointDto> getExpiringPoints(Long memberId, Pageable pageable) {
        
//         List<MemberDto.ExpiringCoins.Res.PointDto> result = query
//             .select(point)
//             .from(point)
//             .join(point.pointDetails, pointDetail)
//             .where(point.member.memberId.eq(memberId))
//             .groupBy(pointDetail.pointAccumId)
//             .having(pointDetail.amount.sum().gt(0))
//             .orderBy(point.expirationDate.asc())
//             .offset(pageable.getOffset())
//             .limit(pageable.getPageSize())
//             .fetch().stream().map(c -> modelMapper.map(c, MemberDto.ExpiringCoins.Res.PointDto.class)).collect(Collectors.toList());
        
//         JPAQuery<Point> countQuery = query
//             .select(point)
//             .from(point)
//             .join(point.pointDetails, pointDetail)
//             .where(point.member.memberId.eq(memberId))
//             .groupBy(pointDetail.pointAccumId)
//             .having(pointDetail.amount.sum().gt(0));
//         Long totalCount = getTotalCountGroupBy(pageable, countQuery);
        
//         return new PageImpl<>(result, pageable, totalCount);
//     }

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