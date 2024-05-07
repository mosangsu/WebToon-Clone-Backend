package com.lezhin.clone.backend.dto;

import com.lezhin.clone.backend.entity.Artist;
import com.lezhin.clone.backend.entity.Timestamped;
import com.lezhin.clone.backend.enums.EpisodeSchedule;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

public class ComicDto {
    public static class Scheduled {

        @Getter
        @Setter
        public static class Res {
            private Long comicId;
        
            private String title;
            private String linkName;
            private String genre;
            private String rating;
            private ArtistDto author;
            private ArtistDto writer;
            private ArtistDto illustrator;
            private ArtistDto originalAuthor;
            private String synopsis;
            private String thumbnail;
            private String cover;
            private String cover2;
            private int order;
        
            private boolean isFree;
            private boolean isPresub;
            private boolean isUpdated;
            private boolean isDelayed;
            private boolean isNew;
            private boolean isEvent;
            private boolean isAwarded;
            private EpisodeSchedule episodeSchedule;
        
            private int freeInterval;
            private String freeMaximum;
            private String freeMinimum;
            private String freeCurrent;
            
            @Getter
            @Setter
            public static class ArtistDto {
                private Long artistId;
                private String name;
                private String linkName;
            }
        }
    }
}