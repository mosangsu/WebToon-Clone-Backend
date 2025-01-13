package com.lezhin.clone.backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Artist extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long artistId;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String linkName;

    @OneToMany(mappedBy = "author")
    private List<Comic> authoredComics;

    @OneToMany(mappedBy = "writer")
    private List<Comic> writtenComics;

    @OneToMany(mappedBy = "illustrator")
    private List<Comic> illustratedComics;
    
    @OneToMany(mappedBy = "originalAuthor")
    private List<Comic> originalComics;

}