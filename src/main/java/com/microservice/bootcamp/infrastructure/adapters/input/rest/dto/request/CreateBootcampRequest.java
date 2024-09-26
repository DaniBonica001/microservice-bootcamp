package com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record CreateBootcampRequest(
        String name,
        String description,
        List<Integer> capacities
) {
}
