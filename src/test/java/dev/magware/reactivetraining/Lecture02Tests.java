package dev.magware.reactivetraining;

import dev.magware.reactivetraining.model.Book;
import dev.magware.reactivetraining.model.BookRepository;
import dev.magware.reactivetraining.openlibrary.OpenLibraryAPI;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@AutoConfigureWebTestClient
public class Lecture02Tests {

    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(9999);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

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
    public void testDetails() throws InterruptedException {
        // configure mock backend
        mockBackEnd.enqueue(
            new MockResponse()
                .setBody("{\"publishers\":[\"publisher1\"],\"latest_revision\": 1}")
                .addHeader("Content-Type", "application/json")
        );

        // make request
        Flux<OpenLibraryAPI.ISBNDetails> response = this.webTestClient
            .get()
                .uri("/lecture02/details/id1")
            .exchange()
                .expectStatus().isOk()
                .returnResult(OpenLibraryAPI.ISBNDetails.class)
                .getResponseBody();

        // check response
        StepVerifier.create(response)
            .expectNext(
                new OpenLibraryAPI.ISBNDetails(
                    List.of("publisher1"),
                    1
                )
            )
            .verifyComplete();

        // check request to mock backend
        RecordedRequest recordedRequest = mockBackEnd.takeRequest();
        Assertions.assertEquals("GET", recordedRequest.getMethod());
        Assertions.assertEquals("/isbn/isbn1.json", recordedRequest.getPath());
    }

}
