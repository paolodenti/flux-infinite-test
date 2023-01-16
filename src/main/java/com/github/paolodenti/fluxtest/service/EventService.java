package com.github.paolodenti.fluxtest.service;

import com.github.paolodenti.fluxtest.dto.SomeDto;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.SynchronousSink;

/**
 * Simulate an event service subscriber (e.g. Kafka, Rabbit, ...)
 */
@Service
@Slf4j
public class EventService {
    private final Random random = new Random(System.currentTimeMillis());

    public void registerSubscriber(SynchronousSink<SomeDto> sink) {
        // sleep to simulate an async event received
        int sleep = sleepSome(3000);
        if (sleep > 2700) { // simulate an <end of stream> condition
            log.info("Reached end of events stream");
            sink.complete();
        } else {
            sink.next(new SomeDto(sleep));
        }
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
