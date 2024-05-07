package com.lezhin.clone.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;

import com.lezhin.clone.backend.enums.EpisodeSchedule;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comic extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comicId;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String linkName;
    @Column(nullable = false)
    private String genre;
    @Column(nullable = false)
    private String rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="authorId")
    private Artist author;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="writerId")
    private Artist writer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="illustratorId")
    private Artist illustrator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="originalAuthorId")
    private Artist originalAuthor;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String synopsis;
    @Column(nullable = false)
    private String thumbnail;
    @Column(nullable = false)
    private String cover;
    @Column(nullable = false)
    private String cover2;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int order;

    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isFree;
    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isPresub;
    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isUpdated;
    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isDelayed;
    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isNew;
    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isEvent;
    @ColumnDefault("0")
    @Column(columnDefinition = "TINYINT")
    private boolean isAwarded;

    @Convert(converter = EpisodeSchedule.EpisodeScheduleAttributeConverter.class)
    private EpisodeSchedule episodeSchedule;

    @Column
    private int freeInterval;
    @Column
    private String freeMaximum;
    @Column
    private String freeMinimum;
    @Column
    private String freeCurrent;

    private LocalDateTime lastUpdatedAt;
}