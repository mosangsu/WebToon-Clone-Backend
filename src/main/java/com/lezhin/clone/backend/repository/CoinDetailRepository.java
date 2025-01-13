package com.lezhin.clone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.CoinDetail;

public interface CoinDetailRepository extends JpaRepository<CoinDetail, Long> {
}