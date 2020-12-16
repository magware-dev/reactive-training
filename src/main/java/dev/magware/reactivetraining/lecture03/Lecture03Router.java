package dev.magware.reactivetraining.lecture03;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Lecture03Router {

    private final Lecture03Handlers lecture03Handlers;

    public Lecture03Router(Lecture03Handlers lecture03Handlers) {
        this.lecture03Handlers = lecture03Handlers;
    }

    @Bean
    public RouterFunction<ServerResponse> lecture03Routes() {
        return RouterFunctions
            .nest(
                RequestPredicates.path("/lecture03/s3"),
                RouterFunctions.route(
                    RequestPredicates.GET("/"),
                    this.lecture03Handlers::list
                ).andRoute(
                    RequestPredicates.POST("/"),
                    this.lecture03Handlers::create
                ).andRoute(
                    RequestPredicates.GET("/{key}"),
                    this.lecture03Handlers::get
                ).andRoute(
                    RequestPredicates.DELETE("/{key}"),
                    this.lecture03Handlers::delete
                )
            );
    }
    
}
