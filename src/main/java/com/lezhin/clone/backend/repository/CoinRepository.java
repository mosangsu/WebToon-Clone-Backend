package com.lezhin.clone.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.lezhin.clone.backend.entity.Coin;
import com.lezhin.clone.backend.repository.jpql.JCoinRepository;
import com.lezhin.clone.backend.repository.jpql.JComicRepository;

public interface CoinRepository extends JpaRepository<Coin, Long>, JCoinRepository {
}