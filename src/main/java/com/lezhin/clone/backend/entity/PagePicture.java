package com.lezhin.clone.backend.entity;

import java.io.Serializable;

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
public class PagePicture {

    
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pageId")
    private Page page;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pictureId")
    private Picture picture;
}

@Getter
@Setter
class PagePictureId implements Serializable {

    private Long page;
    private Long picture;

    @Override
    public boolean equals(Object arg0) {
        return super.equals(arg0);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}