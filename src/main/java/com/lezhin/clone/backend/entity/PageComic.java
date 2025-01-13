package com.lezhin.clone.backend.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class PageComic {

    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pageId")
    private Page page;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comicId")
    private Comic comic;

    @Column(nullable = false)
    private String desc;

    @Column(nullable = false)
    private String subDesc;
}

@Getter
@Setter
class PageComicId implements Serializable {

    private Long page;
    private Long comic;

    @Override
    public boolean equals(Object arg0) {
        return super.equals(arg0);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}