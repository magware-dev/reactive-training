package dev.magware.reactivetraining.lecture01;

import dev.magware.reactivetraining.model.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Stream;

public class FluxAndMono {

    /**
     * Returns the maximum integer of given integers
     */
    public Mono<Integer> max(Integer... integers) {
        // TODO
        return null;
    }

    /**
     * Returns just the ISBN numbers of given books
     */
    public Flux<String> isbn(Stream<Book> books) {
        // TODO
        return null;
    }

    /**
     * Returns only books where the title starts with a given prefix
     */
    public Flux<Book> titleStartsWith(Iterable<Book> books, String prefix) {
        // TODO
        return null;
    }

}
