package com.microservice.bootcamp.application.ports.input;


import com.microservice.bootcamp.domain.model.Bootcamp;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampServicePort {

    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp, List<Integer> capacities);
}
