package dev.magware.reactivetraining.cheatsheet;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class FluxStaticGeneratorTests {

    @Test
    public void just() {
        final AtomicReference<Integer> integer1 = new AtomicReference<>(1);
        final AtomicReference<Integer> integer2 = new AtomicReference<>(2);

        Flux<Integer> integerFlux = Flux.just(
            integer1.get(),
            integer2.get()
        );

        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();

        integer1.set(3);
        integer2.set(4);

        StepVerifier.create(integerFlux)
            .expectNext(1, 2) // !! values stays as it was during creation
            .verifyComplete();
    }

    @Test
    public void defer() {
        final AtomicReference<Integer> integer1 = new AtomicReference<>(1);
        final AtomicReference<Integer> integer2 = new AtomicReference<>(2);

        Flux<Integer> integerFlux = Flux.defer(
            () -> Flux.just(integer1.get(), integer2.get())
        );

        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();

        integer1.set(3);
        integer2.set(4);

        StepVerifier.create(integerFlux)
            .expectNext(3, 4) // !! the supplier gets executed on every subscription
            .verifyComplete();
    }

    @Test
    public void from() {
        // from (publisher)
        Flux<Integer> integerFlux = Flux.from(Flux.just(1, 2));
        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();

        // fromArray
        integerFlux = Flux.fromArray(new Integer[] {1, 2});
        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();

        // fromIterable
        integerFlux = Flux.fromIterable(List.of(1, 2));
        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();

        // fromStream
        integerFlux = Flux.fromStream(List.of(1, 2).stream());
        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();

        // fromStream supplier
        integerFlux = Flux.fromStream(() -> List.of(1, 2).stream());
        StepVerifier.create(integerFlux)
            .expectNext(1, 2)
            .verifyComplete();
    }

}
