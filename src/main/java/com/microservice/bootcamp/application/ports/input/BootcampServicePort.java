package com.microservice.bootcamp.application.ports.input;


import com.microservice.bootcamp.domain.model.Bootcamp;
import org.springframework.data.domain.Page;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampServicePort {

    Mono<Bootcamp> createBootcamp(Bootcamp bootcamp, List<Integer> capacities);
    Mono<Page<Bootcamp>> findAllPaged(int page, int size, String sortBy, String order);
}
