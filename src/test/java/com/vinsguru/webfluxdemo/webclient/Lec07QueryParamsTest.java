package com.vinsguru.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.test.StepVerifier;

import java.net.URI;

/**
 * @author debal
 */
public class Lec07QueryParamsTest  extends BaseTest {
    @Autowired
    private WebClient webClient;

    String queryString = "http://localhost:8080/jobs/search?count={count}&page={page}";

    @Test
    public void queryParamsTest1() {
        URI uri = UriComponentsBuilder.fromUriString(queryString).build(4, 5);
        StepVerifier.create(this.webClient.get().uri(uri)
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(System.out::println))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void queryParamsTest2 () {
        StepVerifier.create(this.webClient.get()
                        .uri(uriBuilder -> uriBuilder.path("jobs/search")
                                .query("count={count}")
                                .query("page={page}")
                                .build(4, 5))
                        .retrieve()
                        .bodyToFlux(Integer.class)
                        .doOnNext(System.out::println))
                .expectNextCount(2)
                .verifyComplete();
    }

}
