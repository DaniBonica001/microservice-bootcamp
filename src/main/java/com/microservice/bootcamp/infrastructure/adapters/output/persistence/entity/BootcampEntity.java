package com.microservice.bootcamp.infrastructure.adapters.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("bootcamp")
public class BootcampEntity {

    @Id
    private int id;
    private String name;
    private String description;
}
