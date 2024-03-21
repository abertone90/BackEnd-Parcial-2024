package com.dh.catalogservice.client;


import com.dh.catalogservice.model.Movie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "movie-service")
public interface IMovieClient {

    @GetMapping("/movie/{genre}")
    List<Movie> getMovieByGenre(@PathVariable String genre);

    @PostMapping("/movie/save")
    Movie saveMovie(@RequestBody Movie movie);

}
