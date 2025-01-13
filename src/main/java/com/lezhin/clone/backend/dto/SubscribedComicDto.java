package com.lezhin.clone.backend.dto;

import java.time.LocalDate;

import org.hibernate.annotations.ColumnDefault;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.enums.MemberType;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

public class SubscribedComicDto {
    public static class Subscribe {

        @Getter
        @Setter
        public static class Res {
            private EpisodeDto comic;
            private MemberDto member;

            @Getter
            @Setter
            public static class EpisodeDto {
                private Long episodeId;
                private String title;
                private String displayName;
                private String thumbnail;
                private int price;
            }

            @Getter
            @Setter
            public static class MemberDto {
                private Long memberId;
                private String nickname;
                private MemberType type;
            }
        }
    }
}