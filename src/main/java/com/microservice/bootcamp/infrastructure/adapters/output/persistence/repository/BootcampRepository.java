package com.microservice.bootcamp.infrastructure.adapters.output.persistence.repository;

import com.microservice.bootcamp.infrastructure.adapters.output.persistence.entity.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface BootcampRepository extends ReactiveCrudRepository<BootcampEntity, Integer> {
    Mono<Boolean> existsByName(String name);
}
