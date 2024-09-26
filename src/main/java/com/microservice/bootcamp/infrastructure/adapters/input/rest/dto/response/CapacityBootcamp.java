package com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record CapacityBootcamp(
        int id,
        String name,
        String description,
        List<Capacity> capacities
) {
}
