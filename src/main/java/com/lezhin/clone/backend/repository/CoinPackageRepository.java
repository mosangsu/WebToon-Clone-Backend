package com.lezhin.clone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.lezhin.clone.backend.entity.CoinPackage;
import com.lezhin.clone.backend.repository.jpql.JCoinPackageRepository;

public interface CoinPackageRepository extends JpaRepository<CoinPackage, Long>, JCoinPackageRepository {
}