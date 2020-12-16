package dev.magware.reactivetraining;

import dev.magware.reactivetraining.model.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;

import java.util.List;
import java.util.concurrent.CompletionException;

@SpringBootTest
@AutoConfigureWebTestClient
public class Lecture03Tests {

    @Autowired
    S3AsyncClient client;

    @Autowired
    WebTestClient webTestClient;

    @Value("${s3.bucket}")
    String bucket;

    List<Book> books = List.of(
        new Book("id1", "book1", "isbn1"),
        new Book("id2", "book2", "isbn2")
    );

    @BeforeEach
    void beforeEachEmptyBucket() {
        // empty bucket
        Flux.from(
            this.client.listObjectsV2Paginator(
                ListObjectsV2Request.builder()
                    .bucket(bucket)
                    .build()
            ).contents()
        )
            .flatMap(
                s3Object -> Mono.fromFuture(
                    this.client.deleteObject(
                        DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(s3Object.key())
                            .build()
                    )
                )
            )
            .blockLast();
    }

    @Test
    public void testList() {
        this.create(this.books.get(0));
        this.create(this.books.get(1));

        this.webTestClient
            .get()
                .uri("/lecture03/s3")
            .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class)
                .hasSize(2)
                .contains(this.books.toArray(new Book[0]));
    }

    @Test
    public void testGet() {
        this.create(this.books.get(0));

        this.webTestClient
            .get()
                .uri("/lecture03/s3/id1")
            .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .isEqualTo(this.books.get(0));
    }

    @Test
    public void testDelete() {
        this.create(this.books.get(0));

        this.webTestClient
            .delete()
                .uri("/lecture03/s3/id1")
            .exchange()
                .expectStatus().isNoContent();

        // check book was actually deleted
        try {
            this.client.headObject(
                HeadObjectRequest.builder()
                    .bucket(bucket)
                    .key(this.books.get(0).getId())
                    .build()
            ).join();
        } catch (CompletionException exception) {
            if (exception.getCause() instanceof NoSuchKeyException) {
                return;
            }
        }

        Assertions.fail("key 'id1' was not deleted");
    }

    private void create(Book book) {
        this.webTestClient
            .post()
                .uri("/lecture03/s3")
                .bodyValue(book)
            .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                    .valueEquals("Location", "/lecture03/s3/" + book.getId());
    }

}
