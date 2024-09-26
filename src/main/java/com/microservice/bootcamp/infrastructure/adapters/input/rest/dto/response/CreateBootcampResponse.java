package com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response;

import lombok.Builder;

@Builder
public record CreateBootcampResponse(
        int id,
        String name,
        String description
) {
}
