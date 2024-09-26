package com.microservice.bootcamp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CapacityBootcamp {
    private int bootcampId;
    private List<Integer> capacities;

}
