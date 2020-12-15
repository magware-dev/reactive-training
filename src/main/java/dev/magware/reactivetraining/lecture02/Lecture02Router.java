package dev.magware.reactivetraining.lecture02;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Lecture02Router {

    private final Lecture02Handlers lecture02Handlers;

    public Lecture02Router(Lecture02Handlers lecture02Handlers) {
        this.lecture02Handlers = lecture02Handlers;
    }

    @Bean
    public RouterFunction<ServerResponse> lecture02Routes() {
        return RouterFunctions
            .route(
                RequestPredicates.GET("/lecture02/details/{id}"),
                this.lecture02Handlers::details
            );
    }
    
}
