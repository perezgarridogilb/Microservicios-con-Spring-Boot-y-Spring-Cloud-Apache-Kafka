package controller;

import java.time.Duration;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;

@RestController
public class NamesController {

    @GetMapping(value = "/names", produces = "text/event-stream")
    public Flux<String> getNames() {
        return Flux.just("one", "two", "three", "four")
                   .delayElements(Duration.ofSeconds(2));
    }
}

