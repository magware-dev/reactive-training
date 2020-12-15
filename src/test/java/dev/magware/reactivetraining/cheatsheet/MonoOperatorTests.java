package dev.magware.reactivetraining.cheatsheet;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
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
        Mono<String> notEmptyStringMono = Mono.just("hello world");
        Mono<String> emptyStringMono = Mono.just("");

        StepVerifier.create(
            notEmptyStringMono.filter(String::isBlank)
        )
            .verifyComplete();

        StepVerifier.create(
            emptyStringMono.filter(String::isBlank)
        )
            .expectNext("")
            .verifyComplete();
    }

    @Test
    public void switchIfEmpty() {
        Mono<String> notEmptyMono = Mono.just("hello world");
        Mono<String> emptyMono = Mono.empty();

        // test not empty
        StepVerifier.create(
            notEmptyMono.switchIfEmpty(
                Mono.error(new RuntimeException("empty mono"))
            )
        )
            .expectNext("hello world")
            .verifyComplete();

        // test empty
        StepVerifier.create(
            emptyMono.switchIfEmpty(
                Mono.error(new RuntimeException("empty mono"))
            )
        )
            .expectErrorMessage("empty mono")
            .verify();
    }

    @Test
    public void flatMap() {
        Mono<String> stringMono = Mono.just("hello world");

        StepVerifier.create(
            stringMono
                .flatMap(
                    s -> Mono.just(s.length())
                )
        )
            .expectNext(11)
            .verifyComplete();
    }

    @Test
    public void flatMapMany() {
        Mono<String> stringMono = Mono.just("hello world");

        StepVerifier.create(
            stringMono
                .flatMapMany(
                    s -> Flux.fromStream(s.chars().boxed())
                )
        )
            .expectNext((int)'h', (int)'e', (int)'l', (int)'l', (int)'o', (int) ' ')
            .expectNext((int)'w', (int)'o', (int)'r', (int)'l', (int)'d')
            .verifyComplete();
    }

}
