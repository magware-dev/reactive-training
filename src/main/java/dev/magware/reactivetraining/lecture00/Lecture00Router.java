package dev.magware.reactivetraining.lecture00;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Lecture00Router {

    private final Lecture00Handlers lecture00Handlers;

    public Lecture00Router(Lecture00Handlers lecture00Handlers) {
        this.lecture00Handlers = lecture00Handlers;
    }

    @Bean
    public RouterFunction<ServerResponse> lecture00Routes() {
        return RouterFunctions
            .route(
                RequestPredicates.GET("/lecture00/findAll"),
                this.lecture00Handlers::findAll
            ); // TODO add route for /lecture00/findOne
    }
    
}
