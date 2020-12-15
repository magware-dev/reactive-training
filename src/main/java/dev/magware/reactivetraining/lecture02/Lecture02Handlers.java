package dev.magware.reactivetraining.lecture02;

import dev.magware.reactivetraining.model.BookRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class Lecture02Handlers {

    private final BookRepository bookRepository;
    private final OpenLibraryAPI openLibraryAPI;

    public Lecture02Handlers(
        BookRepository bookRepository,
        OpenLibraryAPI openLibraryAPI
    ) {
        this.bookRepository = bookRepository;
        this.openLibraryAPI = openLibraryAPI;
    }

    public Mono<ServerResponse> details(ServerRequest request) {
        String id = request.pathVariable("id");
        return this.bookRepository.findById(id)
            .flatMap(
                book -> this.openLibraryAPI
                    .getDetailsByIsbn(book.getIsbn())
            )
            .flatMap(
                details -> ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(details))
            );
    }

}
