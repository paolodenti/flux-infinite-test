package com.github.paolodenti.fluxtest;

import com.github.paolodenti.fluxtest.dto.SomeDto;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class FluxTestApplication implements CommandLineRunner {

    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(FluxTestApplication.class, args);
    }

    /**
     * Simulate a real consumer using a reactive SSE client.
     *
     * @param args args
     */
    @Override
    public void run(String... args) {
        log.info("Consumer test");

        WebClient client = WebClient.create("http://localhost:" + environment.getProperty("server.port"));

        ParameterizedTypeReference<ServerSentEvent<SomeDto>> type
                = new ParameterizedTypeReference<>() {
        };

        Flux<ServerSentEvent<SomeDto>> eventStream = client.get()
                .uri("/sse-stream")
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> log.info("Time: {}: {}", LocalTime.now(), content.data()),
                error -> log.error("Error receiving SSE", error),
                () -> log.info("Stream Completed."));
    }
}
