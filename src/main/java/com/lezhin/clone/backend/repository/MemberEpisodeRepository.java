package com.lezhin.clone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.MemberEpisode;
import com.lezhin.clone.backend.entity.MemberEpiId;

import java.util.List;
import java.util.Optional;


public interface MemberEpisodeRepository extends JpaRepository<MemberEpisode, MemberEpiId> {
  Optional<MemberEpisode> findByMemberMemberIdAndEpisodeEpisodeId(Long memberId, Long comicId);
  List<MemberEpisode> findByMemberMemberIdAndEpisodeComicComicIdIn(Long memberId, List<Long> comicIds);
}