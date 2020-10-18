package dev.magware.reactivetraining;

import dev.magware.reactivetraining.model.Book;
import dev.magware.reactivetraining.model.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Profile("!test")
public class BookDataInitializer implements CommandLineRunner {

    private final BookRepository bookRepository;

    public BookDataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        this.bookRepository
            .deleteAll()
            .thenMany(
                Flux.just(
                    new Book("Reactive Design Patterns", "9781617291807"),
                    new Book("Reactive Application Development", "9781617292460"),
                    new Book("Reactive Web Applications", "9781633430099"),
                    new Book("Functional and Reactive Domain Modeling", "9781617292248"),
                    new Book("The Tao of Microservices", "9781617293146")
                )
            )
            .flatMap(this.bookRepository::save)
            .blockLast();
    }

}
