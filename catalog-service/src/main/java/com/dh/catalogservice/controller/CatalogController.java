package com.dh.catalogservice.controller;
import com.dh.catalogservice.client.IMovieClient;
import com.dh.catalogservice.model.Catalog;
import com.dh.catalogservice.model.Movie;
import com.dh.catalogservice.service.LoadBalancer;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final LoadBalancer loadBalancer;

    @Autowired
    public CatalogController(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Autowired
    private IMovieClient iMovieClient;

    @GetMapping("/{genre}")
    ResponseEntity<Catalog> getMovieByGenre(@PathVariable String genre) {
        // Use the LoadBalancer to choose an instance of "movie-service"
        String loadBalancerResponse = loadBalancer.callOtherService();
        System.out.println("Load Balancer Response: " + loadBalancerResponse);

        List<Movie> movies = iMovieClient.getMovieByGenre(genre);
        Catalog catalog = new Catalog();
        catalog.setGenre(genre);
        catalog.setMovies(movies);
        catalog.setLoadBalancerResponse(loadBalancerResponse);

        return ResponseEntity.ok().body(catalog);
    }


    @PostMapping("/save")
    ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok().body(iMovieClient.saveMovie(movie));
    }

    // For debugging purposes
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World!";
    }
}
