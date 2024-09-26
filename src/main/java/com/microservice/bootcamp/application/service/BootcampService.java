package com.microservice.bootcamp.application.service;

import com.microservice.bootcamp.application.ports.input.BootcampServicePort;
import com.microservice.bootcamp.application.ports.output.BootcampPersistencePort;
import com.microservice.bootcamp.domain.model.Bootcamp;
import com.microservice.bootcamp.domain.model.Capacity;
import com.microservice.bootcamp.domain.model.CapacityBootcamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
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

    @Override
    public Mono<Page<Bootcamp>> findAllPaged(int page, int size, String sortBy, String order) {
        return persistencePort.findAll()
                .switchIfEmpty(Mono.defer(()-> Mono.error(new Exception("Bootcamp not found"))))
                .flatMap(bootcamp -> getCapacitiesByBootcampId(bootcamp.getId())
                        .collectList()
                        .map(capacities -> {
                            bootcamp.setCapacities(capacities);
                            return bootcamp;
                        }))
                .collectList()
                .flatMap(bootcamps -> {
                    List<Bootcamp> sortedBootcamps = sortBootcamps(bootcamps, sortBy, order);
                    int start = page * size;
                    int end = Math.min(start + size, sortedBootcamps.size());
                    List<Bootcamp> pageContent = sortedBootcamps.subList(start, end);
                    return Mono.just(new PageImpl<>(pageContent, PageRequest.of(page, size), sortedBootcamps.size()));
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

    private Flux<Capacity> getCapacitiesByBootcampId(int bootcampId) {
        return webClient.get()
                .uri("/v1/api/{bootcampId}", bootcampId)
                .retrieve()
                .bodyToFlux(Capacity.class)
                .onErrorResume(e -> {
                    log.error("Error fetching capacities for bootcamp {}: {}", bootcampId, e.getMessage());
                    return Flux.empty();
                });
    }

    private List<Bootcamp> sortBootcamps(List<Bootcamp> bootcamps, String sortBy, String order) {
        Comparator<Bootcamp> comparator;
        if ("capacities".equalsIgnoreCase(sortBy)) {
            comparator = Comparator.comparingInt(b -> b.getCapacities().size());
        } else {
            // Default to sorting by name
            comparator = Comparator.comparing(Bootcamp::getName);
        }

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        return bootcamps.stream()
                .sorted(comparator)
                .toList();
    }

}
