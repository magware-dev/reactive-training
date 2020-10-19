package dev.magware.reactivetraining;

import dev.magware.reactivetraining.model.Book;
import dev.magware.reactivetraining.model.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class Lecture00Tests {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    BookRepository bookRepository;

    List<Book> books = List.of(
        new Book("id1", "book1", "isbn1"),
        new Book("id2", "book2", "isbn2")
    );

    @BeforeEach
    void beforeEach() {
        // delete and insert test books
        this.bookRepository
            .deleteAll()
            .thenMany(this.bookRepository.saveAll(this.books))
            .blockLast();
    }

    @Test
    public void testFindAll() {
        // make request
        Flux<Book> response = this.webTestClient
            .get()
                .uri("/lecture00/findAll")
            .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        // check response
        StepVerifier.create(response)
            .expectNext(this.books.toArray(new Book[0]))
            .verifyComplete();
    }

    @Test
    public void testFindOne() {
        // make request
        Flux<Book> response = this.webTestClient
            .get()
                .uri("/lecture00/findOne/id1")
            .exchange()
                .expectStatus().isOk()
                .returnResult(Book.class)
                .getResponseBody();

        // check response
        StepVerifier.create(response)
            .expectNext(this.books.get(0))
            .verifyComplete();
    }

}
