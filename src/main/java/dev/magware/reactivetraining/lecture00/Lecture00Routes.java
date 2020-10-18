package dev.magware.reactivetraining.lecture00;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Lecture00Routes {

    private final Lecture00Handlers handlers;

    public Lecture00Routes(Lecture00Handlers handlers) {
        this.handlers = handlers;
    }

    @Bean
    public RouterFunction<ServerResponse> healthCheck() {
        return RouterFunctions
            .route(
                RequestPredicates.GET("/lecture00/findAll"),
                this.handlers::findAll
            );
    }
    
}
