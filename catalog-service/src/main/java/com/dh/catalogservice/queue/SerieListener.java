package com.dh.catalogservice.queue;

import com.example.mspersona.api.services.PersonaService;
import com.example.mspersona.domain.models.Persona;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SerieListener {

    private final SerieService service;

    public SerieListener(SerieService service) {
        this.service = service;
    }

    @RabbitListener(queues = {"${queue.serie.name}"})
    public void receive(@Payload Serie serie) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        service.save(serie);
    }
}
