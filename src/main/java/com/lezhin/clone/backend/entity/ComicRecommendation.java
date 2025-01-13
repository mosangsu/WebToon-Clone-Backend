package com.lezhin.clone.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ComicRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comicRecommendationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baseComicId")
    private Comic baseComic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommendedComicId")
    private Comic recommendedComic;

}