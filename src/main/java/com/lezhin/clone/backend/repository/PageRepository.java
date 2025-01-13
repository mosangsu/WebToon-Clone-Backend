package com.lezhin.clone.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.entity.Page;
import com.lezhin.clone.backend.repository.jpql.JPageRepository;

import java.util.List;
import java.util.Optional;

public interface PageRepository extends JpaRepository<Page, Long>, JPageRepository{
    Optional<Page> findByLinkName(String linkName);
}