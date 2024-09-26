package com.microservice.bootcamp.application.service;

import com.microservice.bootcamp.application.ports.input.BootcampServicePort;
import com.microservice.bootcamp.application.ports.output.BootcampPersistencePort;
import com.microservice.bootcamp.domain.model.Bootcamp;
import com.microservice.bootcamp.domain.model.CapacityBootcamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BootcampService implements BootcampServicePort {
    private final BootcampPersistencePort persistencePort;
    private final WebClient webClient;

    @Override
    public Mono<Bootcamp> createBootcamp(Bootcamp bootcamp, List<Integer> capacities) {
        return persistencePort.existsByName(bootcamp.getName())
                .flatMap(exists ->{
                    if (exists) {
                        return Mono.error(new Exception("Bootcamp already exists"));
                    }

                    if (capacities.isEmpty() || capacities.size() > 4) {
                        return Mono.error(new Exception("Capacities must be between 1 and 4"));
                    }

                    if (capacities.stream().distinct().count() != capacities.size()) {
                        return Mono.error(new Exception("Capacities must be unique"));
                    }

                    return persistencePort.createBootcamp(bootcamp)
                            .flatMap(createdBootcamp -> associateCapacitiesWithBootcamp(createdBootcamp.getId(), capacities)
                                        .then(Mono.just(createdBootcamp)));
                });
    }

    private Mono<Void> associateCapacitiesWithBootcamp(int bootcampId, List<Integer> capacities){
        return webClient.post()
                .uri("/v1/api/capacity_bootcamp")
                .bodyValue(CapacityBootcamp.builder().bootcampId(bootcampId).capacities(capacities).build())
                .retrieve()
                .bodyToMono(Void.class)
                .onErrorResume(e -> {
                    log.error("Error associating capacities with bootcamp {}: {}", bootcampId, e.getMessage());
                    return Mono.empty();
                });
    }
}
