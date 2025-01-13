package com.lezhin.clone.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.Episode;

public interface EpisodeRepository extends JpaRepository<Episode, Long>{
    Optional<Episode> findByComicLinkNameAndLinkName(String comicLinkName, String linkName);
    Optional<Episode> findTop1ByComicLinkNameAndOrderLessThanOrderByOrderDesc(String comicLinkName, int order);
    Optional<Episode> findTop1ByComicLinkNameAndOrderGreaterThanOrderByOrder(String comicLinkName, int order);
    
}