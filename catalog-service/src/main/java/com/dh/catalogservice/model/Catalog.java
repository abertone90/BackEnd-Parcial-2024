package com.dh.catalogservice.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
public class Catalog {
    private String genre;
    private List<Movie> movies;

    public void setLoadBalancerResponse(String response) {
    }
}
