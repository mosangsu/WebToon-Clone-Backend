package com.lezhin.clone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.LikedComic;
import com.lezhin.clone.backend.entity.MemberComicId;


public interface LikedComicRepository extends JpaRepository<LikedComic, MemberComicId> {
  LikedComic findByComicComicIdAndMemberMemberId(Long comicId, Long memberId);
}