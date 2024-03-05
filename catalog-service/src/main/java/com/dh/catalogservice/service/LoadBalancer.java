package com.dh.catalogservice.service;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

@Service
public class LoadBalancer {

    private final LoadBalancerClient loadBalancerClient;

    public LoadBalancer(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    public String callOtherService() {
        ServiceInstance instance = loadBalancerClient.choose("movie-service");
        int port = instance.getPort(); // Obtén el puerto de la instancia elegida

        return "Respuesta del servicio equilibrado (puerto: " + port + ")";
    }
}
