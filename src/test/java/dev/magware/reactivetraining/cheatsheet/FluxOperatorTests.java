package dev.magware.reactivetraining.cheatsheet;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FluxOperatorTests {

    @Test
    public void reduce() {
        Flux<String> stringFlux = Flux.just("hello", " ", "world");

        Mono<String> joined = stringFlux
            .reduce((intermediate, next) -> intermediate + next);

        StepVerifier.create(joined)
            .expectNext("hello world")
            .verifyComplete();
    }

    @Test
    public void map() {
        Flux<String> stringFlux = Flux.just("hello", " ", "world");

        Flux<Integer> wordCount = stringFlux
            .map(String::length);

        StepVerifier.create(wordCount)
            .expectNext(5, 1, 5)
            .verifyComplete();
    }

    @Test
    public void filter() {
        Flux<String> stringFlux = Flux.just("hello", " ", "world");

        Flux<String> filtered = stringFlux
            .filter(s -> !s.isBlank());

        StepVerifier.create(filtered)
            .expectNext("hello", "world")
            .verifyComplete();

    }

}
