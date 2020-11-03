package dev.magware.reactivetraining.cheatsheet;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class MonoOperatorTests {

    @Test
    public void map() {
        Mono<String> stringMono = Mono.just("hello world");

        Mono<Integer> charCount = stringMono
            .map(String::length);

        StepVerifier.create(charCount)
            .expectNext(11)
            .verifyComplete();
    }

    @Test
    public void filter() {
        Mono<String> notEmptyMono = Mono.just("hello world");
        Mono<String> emptyMono = Mono.just("");

        StepVerifier.create(
            notEmptyMono.filter(String::isBlank)
        )
            .verifyComplete();

        StepVerifier.create(
            emptyMono.filter(String::isBlank)
        )
            .expectNext("")
            .verifyComplete();
    }

}
