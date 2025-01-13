package com.lezhin.clone.backend.repository.jpql;

import static com.lezhin.clone.backend.entity.QComic.comic;
import static com.lezhin.clone.backend.entity.QSubscribedComic.subscribedComic;
import static com.lezhin.clone.backend.entity.QComicTag.comicTag;
import static com.lezhin.clone.backend.entity.QEpisode.episode;
import static com.lezhin.clone.backend.entity.QMemberEpisode.memberEpisode;
import static com.lezhin.clone.backend.entity.QTag.tag;

import jakarta.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.lezhin.clone.backend.dto.ComicDto;
import com.lezhin.clone.backend.dto.ComicDto.Summary.List.Res;
import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.entity.QArtist;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface JComicRepository {
    public Page<ComicDto.Summary.List.Res> findComicList(String tagName, String genre, Boolean isCompleted, Boolean isFree, String order, String q, String t, Pageable pageable);
    public List<ComicDto.Summary.List.Res> findTop100Comics(String genre, Integer year, Boolean isEvent, Boolean isNew);
    public Page<ComicDto.OwnedComic.Res> getOwnedComicList(Long memberId, String q, Pageable pageable);
    public Page<ComicDto.Summary.List.Res> getSubscribedComicList(Long memberId, String q, Pageable pageable);
    public Page<ComicDto.Summary.List.Res> getReadComicList(Integer[] idList, String q, Pageable pageable);
}

@Repository
class JComicRepositoryImpl implements JComicRepository {
    
    @Autowired
    private ModelMapper modelMapper;
    private static final QArtist author = new QArtist("author");
    private static final QArtist illustrator = new QArtist("illustrator");
    private static final QArtist writer = new QArtist("writer");

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JComicRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    
    @Override
    public Page<ComicDto.Summary.List.Res> findComicList(String tagName, String genre, Boolean isCompleted, Boolean isFree, String order, String q, String t, Pageable pageable) {
        JPAQuery<Comic> jpql = query
            .select(comic)
            .from(comic)
            .leftJoin(comic.author, author)
            .leftJoin(comic.illustrator, illustrator)
            .leftJoin(comic.writer, writer)
            .innerJoin(comic.comicTags, comicTag)
            .innerJoin(comicTag.tag, tag)
            .where(tagNameEq(tagName), genreEq(genre), isCompletedEq(isCompleted), isFreeEq(isFree), likeQuery(q, t))
            .distinct();

        if (order != null && order.equals("new"))
            jpql.orderBy(comic.lastUpdatedAt.desc());
        else
            jpql.orderBy(comic.viewCount.desc());

        List<ComicDto.Summary.List.Res> result = jpql
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch().stream().map(c -> modelMapper.map(c, ComicDto.Summary.List.Res.class)).collect(Collectors.toList());

        JPAQuery<Long> countQuery = query
            .select(comic.count())
            .from(comic)
            .leftJoin(comic.author, author)
            .leftJoin(comic.illustrator, illustrator)
            .leftJoin(comic.writer, writer)
            .join(comic.comicTags, comicTag)
            .join(comicTag.tag, tag)
            .where(tagNameEq(tagName), genreEq(genre), isCompletedEq(isCompleted), isFreeEq(isFree), likeQuery(q, t))
            .distinct();

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public List<Res> findTop100Comics(String genre, Integer year, Boolean isEvent, Boolean isNew) {
        List<ComicDto.Summary.List.Res> result = query
            .select(comic)
            .from(comic)
            .join(comic.comicTags, comicTag).fetchJoin()
            .join(comicTag.tag, tag).fetchJoin()
            .where(genreEq(genre), isEventEq(isEvent), isNewEq(isNew), isYearEq(year))
            .limit(100)
            .orderBy(comic.viewCount.desc())
            .fetch().stream().map(c -> modelMapper.map(c, ComicDto.Summary.List.Res.class)).collect(Collectors.toList());
        return result;
    }

    @Override
    public Page<ComicDto.OwnedComic.Res> getOwnedComicList(Long memberId, String q, Pageable pageable) {
        
        JPAQuery<ComicDto.OwnedComic.Res> jpaQuery = query
            .select(
                Projections.bean(ComicDto.OwnedComic.Res.class,
                comic.comicId, // comic_id
                comic.title,   // title
                comic.linkName,
                comic.author.name.as("author"),
                comic.writer.name.as("writer"),
                comic.illustrator.name.as("illustrator"),
                comic.thumbnail,
                memberEpisode.episode.episodeId.countDistinct().as("ownedEpisodes"), // 소장한 에피소드 수
                episode.episodeId.countDistinct().as("totalEpisodes"),       // 전체 에피소드 수
                memberEpisode.episode.episodeId.countDistinct()
                    .multiply(100)
                    .divide(episode.episodeId.countDistinct())
                    .as("percentageOwned")) // 소장 비율
            )
            .from(comic)
            .leftJoin(episode).on(comic.comicId.eq(episode.comic.comicId)) // comic과 episode 조인
            .leftJoin(memberEpisode).on(
                episode.episodeId.eq(memberEpisode.episode.episodeId)
                .and(memberEpisode.member.memberId.eq(memberId))
            ) // episode와 member_episode 조인
            .leftJoin(comic.author, author)
            .leftJoin(comic.writer, writer)
            .leftJoin(comic.illustrator, illustrator)
            .where(likeQuery(q, "title"))
            .groupBy(comic.comicId) // comic_id 기준 그룹화
            .having(memberEpisode.episode.episodeId.countDistinct().gt(0)) // 소장 에피소드 수 > 0
            .orderBy(comic.title.asc());
        List<ComicDto.OwnedComic.Res> result = jpaQuery.fetch();

        Long totalCount = getTotalCountGroupBy(pageable, jpaQuery);

        return new PageImpl<>(result, pageable, totalCount);
    }

    @Override
    public Page<ComicDto.Summary.List.Res> getSubscribedComicList(Long memberId, String q, Pageable pageable) {

        List<ComicDto.Summary.List.Res> result = query
            .select(comic)
            .from(comic)
            .leftJoin(comic.subscribedComics, subscribedComic)
            .where(subscribedComic.member.memberId.eq(memberId), likeQuery(q, "title"))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(comic.viewCount.desc())
            .fetch().stream().map(c -> modelMapper.map(c, ComicDto.Summary.List.Res.class)).collect(Collectors.toList());
            
        JPAQuery<Long> countQuery = query
            .select(comic.count())
            .from(comic)
            .leftJoin(comic.subscribedComics, subscribedComic)
            .where(subscribedComic.member.memberId.eq(memberId), likeQuery(q, "title"))
            .distinct();

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<ComicDto.Summary.List.Res> getReadComicList(Integer[] idList, String q, Pageable pageable) {

        List<ComicDto.Summary.List.Res> result = query
            .select(comic)
            .from(comic)
            .where(comic.comicId.in(idList), likeQuery(q, "title"))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(comic.viewCount.desc())
            .fetch().stream().map(c -> modelMapper.map(c, ComicDto.Summary.List.Res.class)).collect(Collectors.toList());
            
        JPAQuery<Long> countQuery = query
            .select(comic.count())
            .from(comic)
            .where(comic.comicId.in(idList), likeQuery(q, "title"))
            .distinct();

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchOne);
    }

    private Predicate tagNameEq(String tagName) {
        if (tagName == null || tagName.equals("")) return null;
        return comicTag.tag.name.eq(tagName);
    }

    private Predicate genreEq(String genre) {
        if (genre == null || genre.equals("")) return null;
        return comic.genre.eq(genre);
    }

    private Predicate isCompletedEq(Boolean isCompleted) {
        if (isCompleted == null) return null;
        return comic.isCompleted.eq(isCompleted);
    }

    private Predicate isFreeEq(Boolean isFree) {
        if (isFree == null) return null;
        return comic.isFree.eq(isFree);
    }

    private Predicate isEventEq(Boolean isEvent) {
        if (isEvent == null) return null;
        return comic.isEvent.eq(isEvent);
    }

    private Predicate isNewEq(Boolean isNew) {
        if (isNew == null) return null;
        return comic.isNew.eq(isNew);
    }

    private Predicate isYearEq(Integer year) {
        if (year == null) return null;
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59, 59);
        return comic.lastUpdatedAt.between(start, end);
    }

    private BooleanExpression likeQuery(String q, String t) {
        if (q == null || q.equals("")) return null;
        if (t.equals("title"))
            return comic.title.contains(q);
        else if (t.equals("artist"))
            return author.name.contains(q).or(writer.name.contains(q)).or(illustrator.name.contains(q));
        else if (t.equals("tag"))
            return comicTag.tag.name.contains(q);
        else
            return comic.title.contains(q);
        // .or(comic.genre.like("%"+query+"%")).or(comic.author.name.like("%"+query+"%")).or(comic.writer.name.like("%"+query+"%")).or(comic.illustrator.name.like("%"+query+"%"));
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