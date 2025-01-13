package com.lezhin.clone.backend.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Episode extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long episodeId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="comicId")
    private Comic comic;

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String displayName;
    @Column(nullable = false)
    private String thumbnail;

    private LocalDate freeDate;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int order;
    @Column(nullable = false)
    private String linkName;

    @ColumnDefault("0")
    @Column(nullable = false)
    private int price;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EpisodePicture> episodePictures;

    @OneToMany(mappedBy = "episode")
    private List<MemberEpisode> memberEpisodes;
}