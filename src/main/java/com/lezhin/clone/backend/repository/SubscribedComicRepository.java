package com.lezhin.clone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.SubscribedComic;
import com.lezhin.clone.backend.entity.MemberComicId;
import java.util.List;
import java.util.Optional;


public interface SubscribedComicRepository extends JpaRepository<SubscribedComic, MemberComicId> {
  Optional<SubscribedComic> findByMemberMemberIdAndComicComicId(Long memberId, Long comicId);
  List<SubscribedComic> findByMemberMemberIdAndComicComicIdIn(Long memberId, List<Long> comicIds);
}