package dev.magware.reactivetraining.lecture00;

import dev.magware.reactivetraining.model.Book;
import dev.magware.reactivetraining.model.BookRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Lecture00Handlers {

    private final BookRepository bookRepository;

    public Lecture00Handlers(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(this.bookRepository.findAll(), Book.class);
    }

    // TODO add findOne handler

}
