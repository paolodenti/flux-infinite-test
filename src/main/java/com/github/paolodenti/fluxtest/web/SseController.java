package com.github.paolodenti.fluxtest.web;

import com.github.paolodenti.fluxtest.dto.SomeDto;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/sse-stream")
public class SseController {

    private final Random random = new Random(System.currentTimeMillis());

    /**
     * SSE infinite test producer, based on random sleeps.
     *
     * @return an SSE stream
     */
    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<SomeDto> streamTest() {
        return Flux.generate(sink -> {
            int sleep = sleepSome(3000);
            if (sleep > 2700) {
                log.info("Got {}, exiting", sleep);
                sink.complete();
            } else {
                sink.next(new SomeDto(sleep));
            }
        });
    }

    /**
     * Some basic async event simulation.
     *
     * @param bound the max sleep time
     * @return the slept time
     */
    private int sleepSome(int bound) {
        int sleep = random.nextInt(bound);

        try {
            Thread.sleep(sleep);
            return sleep;
        } catch (InterruptedException e) {
            log.error("Just testing ...");
            return 0;
        }
    }
}
