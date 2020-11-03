package dev.magware.reactivetraining;

import dev.magware.reactivetraining.lecture01.FluxAndMono;
import dev.magware.reactivetraining.model.Book;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

public class Lecture01Tests {

    private final FluxAndMono ops = new FluxAndMono();

    @Test
    public void testMax() {
        Mono<Integer> max = this.ops.max(1, 5, 10, 7);

        StepVerifier.create(max)
            .expectNext(10)
            .verifyComplete();
    }

    @Test
    public void testIsbn() {
        List<Book> books = List.of(
            new Book("titel1", "isbn1"),
            new Book("titel2", "isbn2"),
            new Book("titel3", "isbn3")
        );

        Flux<String> isbn = this.ops.isbn(books.stream());

        StepVerifier.create(isbn)
            .expectNext("isbn1", "isbn2", "isbn3")
            .verifyComplete();
    }

    @Test
    public void testTitleStartsWith() {
        List<Book> books = List.of(
            new Book("title1", "isbn1"),
            new Book("prefix_title2", "isbn2"),
            new Book("title3", "isbn3"),
            new Book("prefix_title4", "isbn4")
        );

        Flux<Book> startsWithTitle = this.ops.titleStartsWith(books, "title");
        StepVerifier.create(startsWithTitle)
            .expectNext(books.get(0), books.get(2))
            .verifyComplete();

        Flux<Book> startsWithPrefix = this.ops.titleStartsWith(books, "prefix");
        StepVerifier.create(startsWithPrefix)
            .expectNext(books.get(1), books.get(3))
            .verifyComplete();
    }

}
