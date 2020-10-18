package dev.magware.reactivetraining.lecture00;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Lecture00Routes {

    private final Lecture00Handlers lecture00Handlers;

    public Lecture00Routes(Lecture00Handlers lecture00Handlers) {
        this.lecture00Handlers = lecture00Handlers;
    }

    @Bean
    public RouterFunction<ServerResponse> findAll() {
        return RouterFunctions
            .route(
                RequestPredicates.GET("/lecture00/findAll"),
                this.lecture00Handlers::findAll
            );
    }
    
}
