package com.lezhin.clone.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.dto.ComicDto.Summary.List.Res;
import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.repository.jpql.JComicRepository;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, Long>, JComicRepository{
    List<Comic> findByEpisodeSchedule(EpisodeSchedule episodeSchedule);
    Page<Comic> findByRating(String rating, Pageable pageable);
    Page<Comic> findByIsNew(boolean isNew, Pageable pageable);
    Page<Comic> findByIsCompleted(boolean isCompleted, Pageable pageable);
    Page<Comic> findByIsFree(boolean isFree, Pageable pageable);
    Page<Comic> findByIsPresub(boolean isPresub, Pageable pageable);
    Page<Comic> findByIsUpdated(boolean isUpdated, Pageable pageable);
    Page<Comic> findByIsEvent(boolean isEvent, Pageable pageable);
    Page<Comic> findByIsAwarded(boolean isAwarded, Pageable pageable);
    Page<Comic> findByComicIdInAndTitleLike(int[] idList, String title, Pageable pageable);
    Optional<Comic> findByLinkName(String linkName);
    List<Comic> findTop3AllByOrderByViewCountDesc();
    List<Comic> findTop3ByIsNewOrderByViewCountDesc(boolean isNew);
    List<Comic> findTop3ByIsEventOrderByViewCountDesc(boolean isEvent);
}