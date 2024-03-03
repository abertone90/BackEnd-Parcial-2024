package com.dh.movieservice;

import com.dh.movieservice.model.Movie;
import com.dh.movieservice.repository.MovieRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
@EnableDiscoveryClient
public class MovieServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieServiceApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(MovieRepository repository) {
		return (args) -> {
			if (!repository.findAll().isEmpty()) {
				return;
			}

			repository.save(new Movie(1L, "Freddy vs Krugger", "Terror", "www.netflix.com"));
			repository.save(new Movie(2L, "SAW I", "Terror", "www.netflix.com"));
			repository.save(new Movie(3L, "Piratas del Caribe", "Comedia", "www.netflix.com"));
			repository.save(new Movie(4L, "Interestelar", "Ficcion", "www.netflix.com"));
		};
	}

}
