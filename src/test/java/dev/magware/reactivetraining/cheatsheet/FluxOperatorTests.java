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

    @Test
    public void switchIfEmpty() {
        Flux<String> notEmptyFlux = Flux.just("hello", "world");
        Flux<String> emptyFlux = Flux.empty();

        // test not empty
        StepVerifier.create(
            notEmptyFlux.switchIfEmpty(
                Flux.error(new RuntimeException("empty flux"))
            )
        )
            .expectNext("hello", "world")
            .verifyComplete();

        // test empty
        StepVerifier.create(
            emptyFlux.switchIfEmpty(
                Flux.error(new RuntimeException("empty flux"))
            )
        )
            .expectErrorMessage("empty flux")
            .verify();
    }

    @Test
    public void flatMap() {
        Flux<String> stringFlux = Flux.just("hello", "world");
        Flux<Integer> charFlux = stringFlux
            .flatMap(
                s -> Flux.fromStream(s.chars().boxed())
            );

        StepVerifier.create(charFlux)
            .expectNext((int)'h', (int)'e', (int)'l', (int)'l', (int)'o')
            .expectNext((int)'w', (int)'o', (int)'r', (int)'l', (int)'d')
            .verifyComplete();
    }

}
