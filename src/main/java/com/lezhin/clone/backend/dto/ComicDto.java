package com.lezhin.clone.backend.dto;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import com.lezhin.clone.backend.entity.Artist;
import com.lezhin.clone.backend.entity.Episode;
import com.lezhin.clone.backend.entity.Timestamped;
import com.lezhin.clone.backend.enums.EpisodeSchedule;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    public static class Comic {

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

            private List<EpisodeDto> episodes;
            private List<ComicTagDto> comicTags;
            
            @Getter
            @Setter
            public static class EpisodeDto {
                private Long episodeId;
                private String title;
                private String displayName;
                private String thumbnail;

                private List<EpisodePictureDto> episodePictures;

                @Getter
                @Setter
                public static class EpisodePictureDto {
                    private PictureDto picture;

                    @Getter
                    @Setter
                    public static class PictureDto {
                        private Long pictureId;
                        private String name;
                        private String url;
                        private int fileSize;
                        private int width;
                        private int height;
                    }
                }
            }

            @Getter
            @Setter
            public static class ComicTagDto {
                private TagDto tag;

                @Getter
                @Setter
                public static class TagDto {
                    private Long tagId;
                    private String name;
                }
            }
            
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