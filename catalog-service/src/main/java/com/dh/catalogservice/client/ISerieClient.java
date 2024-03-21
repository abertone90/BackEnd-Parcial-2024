package com.dh.catalogservice.client;

import com.dh.catalogservice.model.Movie;
import com.dh.catalogservice.model.Serie;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "serie-service")
public interface ISerieClient {


    @GetMapping("/serie/{genre}")
    List<Serie> getSerieByGenre(@PathVariable String genre);

    @PostMapping("/serie/save")
    String saveSerie(@RequestBody Serie serie);


    }


