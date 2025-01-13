package com.lezhin.clone.backend.repository.jpql;

import jakarta.persistence.EntityManager;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

public interface JPageRepository {
}

@Repository
class JPageRepositoryImpl implements JPageRepository {
    
    @Autowired
    private ModelMapper modelMapper;

    private final EntityManager em;
    private final JPAQueryFactory query;

    public JPageRepositoryImpl(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
}