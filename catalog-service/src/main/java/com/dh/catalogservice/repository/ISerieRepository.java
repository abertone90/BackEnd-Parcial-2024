package com.dh.catalogservice.repository;

import com.dh.catalogservice.model.Serie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISerieRepository  extends MongoRepository<Serie, String> {
    List<Serie> findByGenre(String genre);

}
