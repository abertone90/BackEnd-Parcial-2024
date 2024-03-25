package com.dh.movieservice.controller;

import com.dh.movieservice.model.Movie;
import com.dh.movieservice.queue.MovieSender;
import com.dh.movieservice.service.MovieService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/movie")
public class MovieController {
    private final MovieService movieService;
    private final MovieSender movieSender;

    public MovieController(MovieService movieService, MovieSender movieSender) {
        this.movieService = movieService;
        this.movieSender = movieSender;
    }

    @GetMapping("/{genre}")
    ResponseEntity<List<Movie>> getMovieByGenre(@PathVariable String genre) {
        List<Movie> movies = movieService.findByGenre(genre);
        return ResponseEntity.ok().body(movies);
    }

    @PostMapping("/save")
    ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        Movie savedMovie = movieService.save(movie);
        movieSender.sendMovie(savedMovie);

        return ResponseEntity.ok().body(savedMovie);
    }
}
