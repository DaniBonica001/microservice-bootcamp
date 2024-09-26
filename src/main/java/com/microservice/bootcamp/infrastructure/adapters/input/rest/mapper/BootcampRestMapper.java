package com.microservice.bootcamp.infrastructure.adapters.input.rest.mapper;

import com.microservice.bootcamp.domain.model.Bootcamp;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.request.CreateBootcampRequest;
import com.microservice.bootcamp.infrastructure.adapters.input.rest.dto.response.CreateBootcampResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BootcampRestMapper {


    Bootcamp fromCreateBootcampRequestToBootcamp(CreateBootcampRequest request);

    CreateBootcampResponse fromBootcampToCreateBootcampResponse(Bootcamp bootcamp);
}
