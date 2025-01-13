package com.lezhin.clone.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.Gift;

public interface GiftRepository extends JpaRepository<Gift, Long> {
    Page<Gift> findByMemberMemberId(Long memberId, Pageable pageable);
    Optional<Gift> getByMemberMemberIdAndGiftId(Long memberId, Long giftId);
}