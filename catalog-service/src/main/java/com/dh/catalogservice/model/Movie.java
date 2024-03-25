package com.dh.catalogservice.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Movie {
    private Long id;
    private String name;
    private String genre;
    private String urlStream;

    public Movie() {
    }
    public Movie(Long id, String name, String genre, String urlStream) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.urlStream = urlStream;
    }

}
