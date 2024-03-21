package com.dh.catalogservice.controller;
import com.dh.catalogservice.client.IMovieClient;
import com.dh.catalogservice.client.ISerieClient;
import com.dh.catalogservice.model.Catalog;
import com.dh.catalogservice.model.Movie;
import com.dh.catalogservice.model.Serie;
import com.dh.catalogservice.service.LoadBalancer;
import com.netflix.discovery.converters.Auto;
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
    private ISerieClient iSerieClient;
    @Autowired
    private IMovieClient iMovieClient;

    @GetMapping("/movie/{genre}")
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


    @PostMapping("/movie/save")
    ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok().body(iMovieClient.saveMovie(movie));
    }


    @PostMapping("/serie/save")
    public ResponseEntity<String> saveSerie(@RequestBody Serie serie) {
        String serieId = iSerieClient.saveSerie(serie);
        return ResponseEntity.ok().body(serieId);
    }



    @GetMapping("/serie/{genre}")
    public ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre) {
        List<Serie> series = iSerieClient.getSerieByGenre(genre);
        return ResponseEntity.ok().body(series);
    }





    // For debugging purposes
    @GetMapping("/hello")
    public String getHello() {
        return "Hello World!";
    }
}
