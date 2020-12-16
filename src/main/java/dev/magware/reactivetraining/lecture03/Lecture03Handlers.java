package dev.magware.reactivetraining.lecture03;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.magware.reactivetraining.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.*;

import java.net.URI;
import java.util.concurrent.CompletionException;

@Component
public class Lecture03Handlers {

    private final S3AsyncClient client;
    private final String bucket;
    private final ObjectMapper objectMapper;

    public Lecture03Handlers(
        S3AsyncClient client,
        @Value("${s3.bucket}") String bucket,
        ObjectMapper objectMapper
    ) {
        this.client = client;
        this.bucket = bucket;
        this.objectMapper = objectMapper;
    }

    /**
     * Initializes buckets on service boot
     */
    @EventListener(classes = ContextRefreshedEvent.class)
    public void initBuckets() throws Throwable {
        try {
            this.client.createBucket(
                CreateBucketRequest.builder()
                    .bucket(bucket)
                    .build()
            ).join();
        } catch (CompletionException exception) {
            // re-throw every exception except when the bucket already exists, which is fine
            if (!(exception.getCause() instanceof BucketAlreadyExistsException)) {
                throw exception.getCause();
            }
        }
    }

    public Mono<ServerResponse> list(ServerRequest request) {
        return Mono.fromFuture(
            this.client.listObjectsV2(
                ListObjectsV2Request.builder()
                    .bucket(this.bucket)
                    .build()
            )
        )
            .flatMapMany(
                listObjects -> Flux.fromIterable(listObjects.contents()))
            .flatMap(
                s3Object -> Mono.fromFuture(
                    this.client.getObject(
                        GetObjectRequest.builder()
                            .bucket(this.bucket)
                            .key(s3Object.key())
                            .build(),
                        AsyncResponseTransformer.toBytes()
                    )
                )
            )
            .flatMap(this::parse)
            .collectList()
            .flatMap(
                content -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(content))
            );
    }

    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(Book.class)
            .flatMap(
                book -> Mono.fromCallable(
                    () -> {
                        try {
                            return this.objectMapper.writeValueAsBytes(book);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    }
                )
                    .map(bytes -> Pair.of(book, bytes))
                    .subscribeOn(Schedulers.boundedElastic())
            )
            .flatMap(
                pair -> Mono.fromFuture(
                    this.client.putObject(
                        PutObjectRequest.builder()
                            .bucket(this.bucket)
                            .key(pair.getFirst().getId())
                            .contentType("application/json")
                            .contentLength((long) pair.getSecond().length)
                            .build(),
                        AsyncRequestBody.fromBytes(pair.getSecond())
                    )
                )
                    .then(Mono.just(pair.getFirst()))
            ).flatMap(
                book -> ServerResponse
                    .created(URI.create("/lecture03/s3/" + book.getId()))
                    .build()
            );
    }

    public Mono<ServerResponse> get(ServerRequest request) {
        return Mono.fromFuture(
            this.client.getObject(
                GetObjectRequest.builder()
                    .bucket(this.bucket)
                    .key(request.pathVariable("key"))
                    .build(),
                AsyncResponseTransformer.toBytes()
            )
        )
            .onErrorResume(NoSuchKeyException.class, throwable -> Mono.empty())
            .flatMap(this::parse)
            .flatMap(
                book -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(book))
            )
            .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        return Mono.fromFuture(
            this.client.deleteObject(
                DeleteObjectRequest.builder()
                    .bucket(this.bucket)
                    .key(request.pathVariable("key"))
                    .build()
            )
        )
            .flatMap(book -> ServerResponse.noContent().build());
    }

    private Mono<Book> parse(ResponseBytes<GetObjectResponse> response) {
        return Mono.fromCallable(
            () -> this.objectMapper.readValue(response.asByteArray(), Book.class)
        )
            .subscribeOn(Schedulers.boundedElastic());
    }

}
