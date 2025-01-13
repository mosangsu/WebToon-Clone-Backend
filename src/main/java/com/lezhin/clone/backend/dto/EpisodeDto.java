package com.lezhin.clone.backend.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class EpisodeDto {
    public static class Detail {

        @Getter
        @Setter
        public static class Res {
            private Long episodeId;

            private ComicDto comic;
            private String title;
            private String displayName;
            private String thumbnail;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
            private LocalDate freeDate;
            private int order;
            private String linkName;
            private int price;
            private String prevLinkName;
            @JsonProperty("isPrev")
            private boolean isPrev;
            private String nextLinkName;
            @JsonProperty("isNext")
            private boolean isNext;

            @Getter
            @Setter
            public static class ComicDto {
                private Long comicId;
                private String title;
                @JsonProperty("isSubscribed")
                private boolean isSubscribed;
            }
        }
    }
    public static class Detail2 {

        @Getter
        @Setter
        public static class Res {
            private Long episodeId;
            private ComicDto comic;
            private String title;
            private String displayName;
            private String thumbnail;
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd", timezone = "Asia/Seoul")
            private LocalDate freeDate;
            private String linkName;
            private int price;
        }
    }

    public static class PaymentException {

        @Getter
        @Setter
        public static class Res {
            private String redirect;
            private String message;
            public Res (String redirect) {
                this.redirect = redirect;
            }
        }
    }
}