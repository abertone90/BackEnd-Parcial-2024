package com.dh.catalogservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "Series")
public class Serie {

    @Id
    private String id;
    private String name;
    private String genre;
    private List<Season> seasons = new ArrayList<>();

    public Serie() {
    }

    public Serie(String id, String name, String genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }



    @Getter
    @Setter
    public static class Season {

        private Integer seasonNumber;
        private List<Chapter> chapters = new ArrayList<>();

        public Season() {
        }

        public Season(Integer seasonNumber, List<Chapter> chapters) {
            this.seasonNumber = seasonNumber;
            this.chapters = chapters;
        }
    }

    @Getter
    @Setter
    public static class Chapter {

        private String name;
        private Integer number;
        private String urlStream;

        public Chapter() {
        }

        public Chapter(String name, Integer number, String urlStream) {
            this.name = name;
            this.number = number;
            this.urlStream = urlStream;
        }
    }
}
