package com.dh.catalogservice.controller;

import com.dh.catalogservice.client.IMovieClient;
import com.dh.catalogservice.model.Catalog;
import com.dh.catalogservice.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    @Autowired
    private IMovieClient iMovieClient;



    @GetMapping("/{genre}")
    ResponseEntity<Catalog> getMovieByGenre(@PathVariable String genre) {
        List<Movie> movies = iMovieClient.getMovieByGenre(genre);
        Catalog response = new Catalog();
        response.setGenre(genre);
        response.setMovies(movies);
        return ResponseEntity.ok().body(response);
    }



    @PostMapping("/save")
    ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok().body(iMovieClient.saveMovie(movie));
    }

    //para debuguear las cosas usamos este hello
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World!";
    }



}
