package com.vinsguru.webfluxdemo;

import com.vinsguru.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

/**
 * @author debal
 */
public class Lec01MultiResponseTest extends BaseTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void testTable() {
        StepVerifier.create(this.webClient
                .get().uri("reactive-math/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class).doOnNext(System.out::println))
                .expectNextCount(10)
                .verifyComplete();
    }

    @Test
    public void testTableStream() {
        StepVerifier.create(this.webClient
                        .get().uri("reactive-math/table/{input}/stream", 5)
                        .retrieve()
                        .bodyToFlux(Response.class).doOnNext(System.out::println))
                .expectNextCount(10)
                .verifyComplete();
    }
}
