package com.vinsguru.webfluxdemo.webtestclient;

import com.vinsguru.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

/**
 * @author debal
 */
@SpringBootTest
@AutoConfigureWebTestClient
public class Lec01SimpleWebTestClient {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void blockTest() {
        final Flux<Response> responseFlux = this.webTestClient
                //Add '/' for web test client!!
                .get().uri("/reactive-math/square/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(Response.class)
                .getResponseBody();
        StepVerifier.create(responseFlux)
                .expectNextMatches(r -> r.getOutput() == 25)
                .verifyComplete();
    }

    @Test
    void fluentAssertionTest() {
        this.webTestClient
                //Add '/' for web test client!!
                .get().uri("/reactive-math/square/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));

    }
}
