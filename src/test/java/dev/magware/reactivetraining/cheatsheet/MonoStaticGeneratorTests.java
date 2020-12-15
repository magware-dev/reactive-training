package dev.magware.reactivetraining.cheatsheet;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class MonoStaticGeneratorTests {

    @Test
    public void just() {
        final AtomicReference<Integer> integer = new AtomicReference<>(1);

        Mono<Integer> integerMono = Mono.just(
            integer.get()
        );

        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        integer.set(2);

        StepVerifier.create(integerMono)
            .expectNext(1) // !! values stays as it was during Mono creation
            .verifyComplete();
    }

    @Test
    public void defer() {
        final AtomicReference<Integer> integer = new AtomicReference<>(1);

        Mono<Integer> integerMono = Mono.defer(
            () -> Mono.just(integer.get())
        );
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        integer.set(2);

        StepVerifier.create(integerMono)
            .expectNext(2) // !! the supplier gets executed on every subscription
            .verifyComplete();
    }

    @Test
    public void from() {
        Mono<Integer> integerMono = Mono.from(Mono.just(1));
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromSupplier
        integerMono = Mono.fromSupplier(() -> 1);
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromCallable
        integerMono = Mono.fromCallable(() -> 1);
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromCompletionStage
        integerMono = Mono.fromCompletionStage(
            CompletableFuture.completedStage(1)
        );
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromCompletionStage supplier
        integerMono = Mono.fromCompletionStage(
            () -> CompletableFuture.completedStage(1)
        );
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromFuture
        integerMono = Mono.fromFuture(
            CompletableFuture.completedFuture(1)
        );
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromFuture supplier
        integerMono = Mono.fromFuture(
            () -> CompletableFuture.completedFuture(1)
        );
        StepVerifier.create(integerMono)
            .expectNext(1)
            .verifyComplete();

        // fromRunnable
        integerMono = Mono.fromRunnable(() -> {});
        StepVerifier.create(integerMono)
            .verifyComplete();
    }

    @Test
    public void empty() {
        Mono<Integer> emptyMono = Mono.empty();

        StepVerifier.create(emptyMono)
            .verifyComplete();
    }

    @Test
    public void error() {
        Mono<Integer> errorMono = Mono.error(new Exception("exception"));

        StepVerifier.create(errorMono)
            .verifyErrorMessage("exception");
    }

}
