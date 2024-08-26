package com.lezhin.clone.backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@IdClass(EpisodePictureId.class)
@NoArgsConstructor
public class EpisodePicture {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "episodeId")
    private Episode episode;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pictureId")
    private Picture picture;

    public EpisodePicture(Episode episode, Picture picture) {
        this.episode = episode;
        this.picture = picture;
    }
}
