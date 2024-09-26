package com.microservice.bootcamp.infrastructure.adapters.output.persistence;

import com.microservice.bootcamp.application.ports.output.BootcampPersistencePort;
import com.microservice.bootcamp.domain.model.Bootcamp;
import com.microservice.bootcamp.infrastructure.adapters.output.persistence.mapper.BootcampPersistenceMapper;
import com.microservice.bootcamp.infrastructure.adapters.output.persistence.repository.BootcampRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements BootcampPersistencePort {
    private final BootcampRepository repository;
    private final BootcampPersistenceMapper mapper;

    @Override
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp) {
        return repository.save(mapper.fromBootcampToBootcampEntity(bootcamp)).map(mapper::fromBootcampEntityToBootcamp);
    }

    @Override
    public Mono<Boolean> existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Flux<Bootcamp> findAll() {
        return repository.findAll().map(mapper::fromBootcampEntityToBootcamp);
    }
}
