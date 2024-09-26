package com.microservice.bootcamp.infrastructure.adapters.output.persistence.mapper;

import com.microservice.bootcamp.domain.model.Bootcamp;
import com.microservice.bootcamp.infrastructure.adapters.output.persistence.entity.BootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BootcampPersistenceMapper {

    BootcampEntity fromBootcampToBootcampEntity(Bootcamp bootcamp);

    Bootcamp fromBootcampEntityToBootcamp(BootcampEntity bootcampEntity);
}
