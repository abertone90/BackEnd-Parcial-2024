package com.dh.catalogservice.queue;


import com.dh.catalogservice.client.ISerieClient;
import com.dh.catalogservice.model.Serie;
import com.dh.catalogservice.repository.ISerieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class SerieListener {

    private final ISerieRepository iSerieRepository;

    @Autowired
    public SerieListener(ISerieRepository serieRepository) {
        this.iSerieRepository = serieRepository;
    }

    @RabbitListener(queues = "${queue.serie.name}")
    public void receive(String serieJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Serie serie = objectMapper.readValue(serieJson, Serie.class);

            iSerieRepository.save(serie);

            System.out.println("Serie guardada en la base de datos: " + serie.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}