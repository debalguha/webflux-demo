package com.vinsguru.webfluxdemo.webclient.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * @author debal
 */
@Configuration
@Slf4j
public class WebClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                //.defaultHeaders(h -> h.setBasicAuth("someKey", "someValue"))
                .filter(this::sessionTokenGenerator)
                .filter(logRequest())
                .build();
    }

/*
    private Mono<ClientResponse> sessionTokenGenerator(ClientRequest req, ExchangeFunction fun) {
        System.out.println("Generating session token");
        final ClientRequest clientRequest = ClientRequest.from(req)
                .headers(h -> h.setBearerAuth("Some-jwt-token-set")).build();
        return fun.exchange(clientRequest);
    }
*/

    private Mono<ClientResponse> sessionTokenGenerator(ClientRequest req, ExchangeFunction fun) {
        //auth - basic or oauth
        final ClientRequest clientRequest = Optional.of(req)
                .flatMap(r -> r.attribute("auth"))
                .map(v -> "basic".equals(v) ? withBasicAuth(req) : withOAuth(req))
                .orElse(req);

        return fun.exchange(clientRequest);
    }

    private ClientRequest withBasicAuth(ClientRequest clientRequest) {
        return ClientRequest.from(clientRequest)
                .headers(h -> h.setBasicAuth("SomeUser", "SomePass")).build();
    }

    private ClientRequest withOAuth(ClientRequest clientRequest) {
        return ClientRequest.from(clientRequest)
                .headers(h -> h.setBearerAuth("some-jwt-token")).build();
    }

    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
            return Mono.just(clientRequest);
        });
    }
}
