package com.dh.catalogservice.controller;
import com.dh.catalogservice.client.IMovieClient;
import com.dh.catalogservice.client.ISerieClient;
import com.dh.catalogservice.model.Catalog;
import com.dh.catalogservice.model.Movie;
import com.dh.catalogservice.model.Serie;
import com.dh.catalogservice.repository.IMovieRepository;
import com.dh.catalogservice.repository.ISerieRepository;
import com.dh.catalogservice.service.LoadBalancer;
import com.netflix.discovery.converters.Auto;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/catalog")
public class CatalogController {


    /**
     * Implementación del circuit breaker para gestionar fallos en el servicio de películas o series.
     * Si el servicio de película o serie está desconectado o falla al arrojar una excepción tipo Feign,
     * se intentará la misma acción hasta tres veces. Si continúa fallando, se aumentará el contador de fallos.
     * Cuando el contador de fallos alcanza un porcentaje determinado, el circuito se cerrará durante 15 segundos
     * antes de volver a aceptar solicitudes. Se mostrará un mensaje indicando que el servicio está temporalmente
     * no disponible en caso de fallo tras los reintentos.
     */



    private final LoadBalancer loadBalancer;

    @Autowired
    public CatalogController(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Autowired
    private ISerieClient iSerieClient;
    @Autowired
    private IMovieClient iMovieClient;

    @Autowired
    private IMovieRepository iMovieRepository;

    @Autowired
    private ISerieRepository iSerieRepository;



//endpoint for filter movie by genre in his own database
    @CircuitBreaker(name = "catalog", fallbackMethod = "movieServiceFallbackMethod")
    @Retry(name = "catalog")
    @GetMapping("/movie/{genre}")
    ResponseEntity<Catalog> getMovieByGenre(@PathVariable String genre) {
        String loadBalancerResponse = loadBalancer.callOtherService();
        System.out.println("Load Balancer Response: " + loadBalancerResponse);

        List<Movie> movies = iMovieClient.getMovieByGenre(genre);
        Catalog catalog = new Catalog();
        catalog.setGenre(genre);
        catalog.setMovies(movies);
        catalog.setLoadBalancerResponse(loadBalancerResponse);

        return ResponseEntity.ok().body(catalog);
    }




    @CircuitBreaker(name = "catalog", fallbackMethod = "movieServiceFallbackMethod")
    @Retry(name = "catalog")
    @PostMapping("/movie/save")
    ResponseEntity<Movie> saveMovie(@RequestBody Movie movie) {
        return ResponseEntity.ok().body(iMovieClient.saveMovie(movie));
    }

    @CircuitBreaker(name = "catalog", fallbackMethod = "serieServiceFallbackMethod")
    @Retry(name = "catalog")
    @PostMapping("/serie/save")
    public ResponseEntity<String> saveSerie(@RequestBody Serie serie) {
        String serieId = iSerieClient.saveSerie(serie);
        return ResponseEntity.ok().body(serieId);
    }


    //endpoint for filter serie by genre in his own database
    @CircuitBreaker(name = "catalog", fallbackMethod = "serieServiceFallbackMethod")
    @Retry(name = "catalog")
    @GetMapping("/serie/{genre}")
    public ResponseEntity<List<Serie>> getSerieByGenre(@PathVariable String genre) {
        List<Serie> series = iSerieClient.getSerieByGenre(genre);
        return ResponseEntity.ok().body(series);
    }

    // Fallback method for serie down
    private ResponseEntity<String> serieServiceFallbackMethod(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Serie service is currently unavailable");
    }

    // Fallback method for movie down
   private ResponseEntity<String> movieServiceFallbackMethod(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Movie service is currently unavailable");
    }


    // Fallback method for Catalog down
    private ResponseEntity<String> catalogServiceFallbackMethod(Exception e) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body("Catalog service is currently unavailable");
    }


    //endpoint for filter movie in catalog database
    @CircuitBreaker(name = "catalog", fallbackMethod = "catalogServiceFallbackMethod")
    @Retry(name = "catalog")
    @GetMapping("/v1/movie/{genre}")
    ResponseEntity<List<Movie>> getMoviesByGenre(@PathVariable String genre) {
        List<Movie> movies = iMovieRepository.findByGenre(genre);
        return ResponseEntity.ok().body(movies);
    }

    //endpoint for filter serie in catalog database
    @CircuitBreaker(name = "catalog", fallbackMethod = "catalogServiceFallbackMethod")
    @Retry(name = "catalog")
    @GetMapping("/v1/serie/{genre}")
    ResponseEntity<List<Serie>> getSeriesByGenre(@PathVariable String genre) {
        List<Serie> series = iSerieRepository.findByGenre(genre);
        return ResponseEntity.ok().body(series);
    }

}
