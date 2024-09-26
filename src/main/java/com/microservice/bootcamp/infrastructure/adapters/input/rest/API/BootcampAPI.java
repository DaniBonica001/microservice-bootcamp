package com.microservice.bootcamp.infrastructure.adapters.input.rest.API;

import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.request.CreateBootcampRequest;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response.CapacityBootcamp;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response.CreateBootcampResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequestMapping(BootcampAPI.BASE_URL)
public interface BootcampAPI {

    String BASE_URL = "/bootcamps";

    @PostMapping("/v1/api")
    Mono<CreateBootcampResponse> createBootcamp(@RequestBody CreateBootcampRequest request);

    @GetMapping("/v1/api")
    Mono<Page<CapacityBootcamp>> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String order);
}
