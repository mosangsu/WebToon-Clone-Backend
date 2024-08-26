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
import java.io.Serializable;

@Entity
@Getter
@Setter
@IdClass(ComicTagId.class)
@NoArgsConstructor
public class ComicTag {
    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comicId")
    private Comic comic;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tagId")
    private Tag tag;

    public ComicTag(Comic comic, Tag tag) {
        this.comic = comic;
        this.tag = tag;
    }
}

@Getter
@Setter
class ComicTagId implements Serializable {

    private Long comic;
    private Long tag;

    @Override
    public boolean equals(Object arg0) {
        return super.equals(arg0);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}