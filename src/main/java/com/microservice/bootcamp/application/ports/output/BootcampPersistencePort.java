package com.microservice.bootcamp.application.ports.output;


import com.microservice.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampPersistencePort {

    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp);
    Mono<Boolean> existsByName(String name);
}
