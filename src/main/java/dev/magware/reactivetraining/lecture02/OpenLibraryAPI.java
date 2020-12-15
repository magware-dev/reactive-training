package dev.magware.reactivetraining.lecture02;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.util.List;

@Component
@Log4j2
public class OpenLibraryAPI {

    private static final String isbnApi = "/isbn/{ISBN}.json";

    private final WebClient webClient;

    public OpenLibraryAPI(
        @Value("${openLibrary.apiBaseUrl}") String apiBaseUrl,
        WebClient.Builder webClientBuilder
    ) {
        this.webClient = webClientBuilder
            .clientConnector(
                new ReactorClientHttpConnector(
                    HttpClient.create()
                        .followRedirect(true)
                )
            )
            .baseUrl(apiBaseUrl)
            .build();
    }

    public Mono<ISBNDetails> getDetailsByIsbn(String isbn) {
        // TODO
        return null;
    }

    @Data
    @AllArgsConstructor
    public static class ISBNDetails {
        private List<String> publishers;

        @JsonProperty("latest_revision")
        private Integer latestRevision;
    }

}
