package com.microservice.bootcamp.infrastructure.adapters.input.rest;


import com.microservice.bootcamp.application.ports.input.BootcampServicePort;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.API.BootcampAPI;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.request.CreateBootcampRequest;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response.CapacityBootcamp;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response.CreateBootcampResponse;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.mapper.BootcampRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BootcampController implements BootcampAPI {

    private final BootcampServicePort bootcampService;
    private final BootcampRestMapper mapper;

    @Override
    public Mono<CreateBootcampResponse> createBootcamp(CreateBootcampRequest request) {
        return bootcampService.createBootcamp(mapper.fromCreateBootcampRequestToBootcamp(request), request.capacities())
                .map(mapper::fromBootcampToCreateBootcampResponse);
    }

    @Override
    public Mono<Page<CapacityBootcamp>> findAll(int page, int size, String sortBy, String order) {
        return bootcampService.findAllPaged(page,size,sortBy,order)
                .map(pageBootcamp -> pageBootcamp.map(mapper::fromBootcampToCapacityBootcamp));
    }
}
