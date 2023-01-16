package com.github.paolodenti.fluxtest.web;

import com.github.paolodenti.fluxtest.dto.SomeDto;
import com.github.paolodenti.fluxtest.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/sse-stream")
@RequiredArgsConstructor
public class SseController {

    private final EventService eventService;

    /**
     * SSE infinite test producer, based on random sleeps.
     *
     * @return an SSE stream
     */
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<SomeDto>> streamTest() {
        return Flux.generate(eventService::registerSubscriber)
                .map(e -> ServerSentEvent.<SomeDto>builder().data(e).build());
    }
}
