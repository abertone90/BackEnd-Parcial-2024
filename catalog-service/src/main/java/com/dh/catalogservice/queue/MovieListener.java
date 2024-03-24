package com.dh.catalogservice.queue;

import com.example.mspersona.api.services.PersonaService;
import com.example.mspersona.domain.models.Persona;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MovieListener {

    private final MovieService service;

    public MovieListener(MovieService service) {
        this.service = service;
    }

    @RabbitListener(queues = {"${queue.movie.name}"})
    public void receive(@Payload Movie movie) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.save(movie);
    }
}
