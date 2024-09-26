package com.microservice.bootcamp.infrastructure.adapters.input.rest.API;

import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.request.CreateBootcampRequest;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response.CreateBootcampResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;

@RequestMapping(BootcampAPI.BASE_URL)
public interface BootcampAPI {

    String BASE_URL = "/bootcamps";

    @PostMapping("/v1/api")
    Mono<CreateBootcampResponse> createBootcamp(@RequestBody CreateBootcampRequest request);
}
