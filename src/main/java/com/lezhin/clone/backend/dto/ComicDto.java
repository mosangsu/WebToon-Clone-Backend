package com.lezhin.clone.backend.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lezhin.clone.backend.entity.ComicRecommendation;
import com.lezhin.clone.backend.enums.EpisodeSchedule;

import jakarta.persistence.OneToMany;
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

            @JsonProperty("isCompleted")
            private boolean isCompleted;
            @JsonProperty("isFree")
            private boolean isFree;
            @JsonProperty("isPresub")
            private boolean isPresub;
            @JsonProperty("isUpdated")
            private boolean isUpdated;
            @JsonProperty("isDelayed")
            private boolean isDelayed;
            @JsonProperty("isNew")
            private boolean isNew;
            @JsonProperty("isEvent")
            private boolean isEvent;
            @JsonProperty("isAwarded")
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

    public static class Ranking {

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
        
            @JsonProperty("isCompleted")
            private boolean isCompleted;
            @JsonProperty("isFree")
            private boolean isFree;
            @JsonProperty("isPresub")
            private boolean isPresub;
            @JsonProperty("isUpdated")
            private boolean isUpdated;
            @JsonProperty("isDelayed")
            private boolean isDelayed;
            @JsonProperty("isNew")
            private boolean isNew;
            @JsonProperty("isEvent")
            private boolean isEvent;
            @JsonProperty("isAwarded")
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

    public static class Summary {

        @Getter
        @Setter
        public static class List {
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
                @JsonProperty("isCompleted")
                private boolean isCompleted;
                @JsonProperty("isFree")
                private boolean isFree;
                @JsonProperty("isPresub")
                private boolean isPresub;
                @JsonProperty("isUpdated")
                private boolean isUpdated;
                @JsonProperty("isDelayed")
                private boolean isDelayed;
                @JsonProperty("isNew")
                private boolean isNew;
                @JsonProperty("isEvent")
                private boolean isEvent;
                @JsonProperty("isAwarded")
                private boolean isAwarded;
                private EpisodeSchedule episodeSchedule;
            
                private int freeInterval;
                private String freeMaximum;
                private String freeMinimum;
                private String freeCurrent;

                private java.util.List<ComicTagDto> comicTags;
                
                @Getter
                @Setter
                public static class ArtistDto {
                    private Long artistId;
                    private String name;
                    private String linkName;
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
        
            @JsonProperty("isCompleted")
            private boolean isCompleted;
            @JsonProperty("isFree")
            private boolean isFree;
            @JsonProperty("isPresub")
            private boolean isPresub;
            @JsonProperty("isUpdated")
            private boolean isUpdated;
            @JsonProperty("isDelayed")
            private boolean isDelayed;
            @JsonProperty("isNew")
            private boolean isNew;
            @JsonProperty("isEvent")
            private boolean isEvent;
            @JsonProperty("isAwarded")
            private boolean isAwarded;
            private EpisodeSchedule episodeSchedule;
        
            private int freeInterval;
            private String freeMaximum;
            private String freeMinimum;
            private String freeCurrent;

            private List<EpisodeDto> episodes;
            private List<ComicTagDto> comicTags;
            @JsonProperty("isSubscribed")
            private boolean isSubscribed;
            @JsonProperty("isLiked")
            private Boolean isLiked;

            private List<ComicRecommendationDto> comicRecommendations;
            
            @Getter
            @Setter
            public static class EpisodeDto {
                private Long episodeId;
                private String title;
                private String displayName;
                private String thumbnail;
                
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
                private LocalDate freeDate;
                private int order;
                private String linkName;
                private int price;

                @JsonProperty("isOwned")
                private boolean isOwned;

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
            public static class ComicRecommendationDto {
                private ComicSummaryDto recommendedComic;
            
                @Getter
                @Setter
                public static class ComicSummaryDto {
                    private String title;
                    private String linkName;
                    private String thumbnail;
                    private ArtistSummaryDto author;
                    private ArtistSummaryDto writer;
                    private ArtistSummaryDto illustrator;
                    private ArtistSummaryDto originalAuthor;
            
                    @Getter
                    @Setter
                    public static class ArtistSummaryDto {
                        private String name;
                    }
                }
            }
            
            @Getter
            @Setter
            public static class ArtistDto {
                private Long artistId;
                private String name;
                private String linkName;
                
                private List<ComicSummaryDto> authoredComics;
                private List<ComicSummaryDto> writtenComics;
                private List<ComicSummaryDto> illustratedComics;
                private List<ComicSummaryDto> originalComics;
            
                @Getter
                @Setter
                public static class ComicSummaryDto {
                    private String title;
                    private String linkName;
                    private String thumbnail;
                    private ArtistSummaryDto author;
                    private ArtistSummaryDto writer;
                    private ArtistSummaryDto illustrator;
                    private ArtistSummaryDto originalAuthor;
            
                    @Getter
                    @Setter
                    public static class ArtistSummaryDto {
                        private String name;
                    }
                }
            }
        }
    }

    public static class Subscribe {

        @Getter
        @Setter
        public static class Req {
            private Long comicId;
        }
    }

    public static class Unsubscribe {

        @Getter
        @Setter
        public static class Req {
            private List<Long> comicIds;
        }
    }

    public static class Disown {

        @Getter
        @Setter
        public static class Req {
            private List<Long> comicIds;
        }
    }

    public static class Like {

        @Getter
        @Setter
        public static class Req {
            private Long comicId;
            @JsonProperty("isLiked")
            private boolean isLiked;
        }
    }

    public static class OwnedComic {

        @Getter
        @Setter
        public static class Res {
            private Long comicId;
            private String title;
            private String linkName;
            private String author;
            private String writer;
            private String illustrator;
            private String thumbnail;
            private Long ownedEpisodes;
            private Long totalEpisodes;
            private Long percentageOwned; 
        }
    }
}