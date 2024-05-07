package com.lezhin.clone.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.entity.Member;
import com.lezhin.clone.backend.enums.EpisodeSchedule;

import java.util.List;

public interface ComicRepository extends JpaRepository<Comic, Long> {
    List<Comic> findByEpisodeSchedule(EpisodeSchedule episodeSchedule);
}