package com.microservice.bootcamp.unit;

import com.microservice.bootcamp.application.ports.output.BootcampPersistencePort;
import com.microservice.bootcamp.application.service.BootcampService;
import com.microservice.bootcamp.domain.model.Bootcamp;
import com.microservice.bootcamp.domain.model.Capacity;
import com.microservice.bootcamp.domain.model.CapacityBootcamp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BootcampServiceTests {
    @Mock
    private BootcampPersistencePort persistencePort;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private BootcampService service;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        service = new BootcampService(persistencePort, webClient);
    }

    @Test
    void testCreateBootcampSuccess() {
        // Arrange
        Bootcamp bootcamp = Bootcamp.builder().name("New Bootcamp").build();
        List<Integer> capacities = Arrays.asList(1, 2); // 2 capacities, valid

        // Mocking persistencePort
        when(persistencePort.existsByName(anyString())).thenReturn(Mono.just(false));
        when(persistencePort.createBootcamp(any(Bootcamp.class))).thenReturn(Mono.just(bootcamp));

        // Mocking WebClient chain
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.bodyValue(any(CapacityBootcamp.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(service.createBootcamp(bootcamp, capacities))
                .expectNext(bootcamp)
                .verifyComplete();

        verify(persistencePort).existsByName(bootcamp.getName());
        verify(persistencePort).createBootcamp(bootcamp);
    }
    @Test
    void testCreateBootcampAlreadyExists() {
        // Arrange
        Bootcamp bootcamp = Bootcamp.builder().name("Existing Bootcamp").build();
        List<Integer> capacities = Arrays.asList(1, 2); // 2 capacities

        when(persistencePort.existsByName(bootcamp.getName())).thenReturn(Mono.just(true));

        // Act & Assert
        StepVerifier.create(service.createBootcamp(bootcamp, capacities))
                .expectErrorMatches(e -> e.getMessage().equals("Bootcamp already exists"))
                .verify();

        verify(persistencePort).existsByName(bootcamp.getName());
        verify(persistencePort, never()).createBootcamp(any());
    }

    @Test
    void testCreateBootcampInvalidCapacitiesCount() {
        // Arrange
        Bootcamp bootcamp = Bootcamp.builder().name("New Bootcamp").build();
        List<Integer> capacities = Arrays.asList(1,2,3,4,5); // 0 capacities (invalid)

        when(persistencePort.existsByName(bootcamp.getName())).thenReturn(Mono.just(false));

        // Act & Assert
        StepVerifier.create(service.createBootcamp(bootcamp, capacities))
                .expectErrorMatches(e -> e.getMessage().equals("Capacities must be between 1 and 4"))
                .verify();

        verify(persistencePort).existsByName(bootcamp.getName());
        verify(persistencePort, never()).createBootcamp(any(Bootcamp.class));
    }

    @Test
    void testFindAllPagedSuccess() {
        // Arrange
        Bootcamp bootcamp1 = Bootcamp.builder().id(1).name("Bootcamp 1").build();
        Bootcamp bootcamp2 = Bootcamp.builder().id(2).name("Bootcamp 2").build();
        List<Bootcamp> bootcamps = Arrays.asList(bootcamp1, bootcamp2);

        Capacity capacity1 = Capacity.builder().name("Capacity 1").build();
        Capacity capacity2 = Capacity.builder().name("Capacity 2").build();
        List<Capacity> capacities = Arrays.asList(capacity1, capacity2);

        // Mocks de WebClient
        WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpecMock = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec<?> requestHeadersSpecMock = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpecMock = mock(WebClient.ResponseSpec.class);

        // Mock comportamiento del WebClient
        when(webClient.get()).thenReturn((WebClient.RequestHeadersUriSpec) requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString(), anyInt())).thenAnswer(invocation -> requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToFlux(Capacity.class)).thenReturn(Flux.fromIterable(capacities));

        // Mock persistence behavior
        when(persistencePort.findAll()).thenReturn(Flux.fromIterable(bootcamps));

        // Act & Assert
        StepVerifier.create(service.findAllPaged(0, 2, "name", "asc"))
                .expectNextMatches(page -> page.getContent().size() == 2 &&
                        page.getContent().get(0).getCapacities().size() == 2)
                .verifyComplete();

        verify(persistencePort).findAll();
    }

    @Test
    void testFindAllPagedBootcampNotFound() {
        // Arrange
        when(persistencePort.findAll()).thenReturn(Flux.empty());

        // Act & Assert
        StepVerifier.create(service.findAllPaged(0, 2, "name", "asc"))
                .expectErrorMatches(throwable -> throwable instanceof Exception && throwable.getMessage().equals("Bootcamp not found"))
                .verify();

        verify(persistencePort).findAll();
    }








}
