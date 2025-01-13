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
@IdClass(MemberComicId.class)
@NoArgsConstructor
public class LikedComic {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comicId")
    private Comic comic;

    private boolean isLiked;

    public LikedComic(Member member, Comic comic, boolean isLiked) {
        this.member = member;
        this.comic = comic;
        this.isLiked = isLiked;
    }
}

