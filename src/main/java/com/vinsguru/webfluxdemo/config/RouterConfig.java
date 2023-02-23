package com.vinsguru.webfluxdemo.config;

import com.vinsguru.webfluxdemo.dto.InputFailedValidationResponse;
import com.vinsguru.webfluxdemo.exception.InputValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * @author debal
 */
@Configuration
public class RouterConfig {
    @Bean
    public RouterFunction<ServerResponse> router(RequestHandler requestHandler) {
        return RouterFunctions.route()
                .path("router/table", () -> tableRouter(requestHandler))
                .path("router/square", () -> squareRouter(requestHandler))
                .POST("router/multiply", requestHandler::multiplyHandler)
                .path("router/calc", () -> calculator(requestHandler))
                .onError(InputValidationException.class, handleError())
                .build();
    }

    private RouterFunction<ServerResponse> calculator(RequestHandler requestHandler) {
        return RouterFunctions.route()
                .GET("{input1}/{input2}", RequestPredicates.headers(hs -> !hs.header("OP").isEmpty()),requestHandler:: calculator)
                .GET("{input1}/{input2}", RequestPredicates.all(), req -> ServerResponse.badRequest().bodyValue("Invalid request or operation!!"))
                .build();
    }

    public RouterFunction<ServerResponse> squareRouter(RequestHandler requestHandler) {
        return RouterFunctions.route()
                .GET("{input}", requestHandler::squareHandler)
                .GET("{input}/validation", RequestPredicates.path("-?/validation")
                        .or(RequestPredicates.path(".*\\.?/validation")), req -> ServerResponse.badRequest().bodyValue("Negative numbers or decimals are not allowed!!"))
                .GET("{input}/validation", requestHandler::squareHandlerWithValidation)
                .build();
    }
    public RouterFunction<ServerResponse> tableRouter(RequestHandler requestHandler) {
        return RouterFunctions.route()
                .GET("{input}", requestHandler::tableHandler)
                .GET("{input}/stream", requestHandler::tableStreamHandler)
                .build();
    }


    private static BiFunction<InputValidationException, ServerRequest, Mono<ServerResponse>> handleError() {
        return (e, request) -> ServerResponse.badRequest().bodyValue(InputFailedValidationResponse
                .builder()
                .input(e.getInput())
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .build()
        );
    }
}
