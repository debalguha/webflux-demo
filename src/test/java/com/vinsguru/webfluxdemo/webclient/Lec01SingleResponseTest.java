package com.vinsguru.webfluxdemo.webclient;

import com.vinsguru.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

/**
 * @author debal
 */
public class Lec01SingleResponseTest extends BaseTest {


    @Autowired
    private WebClient webClient;

    @Test
    public void blockTest() {
        final Response response = this.webClient
                .get().uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class)
                .block();
        Assertions.assertEquals(25, response.getOutput());
    }

    @Test
    public void nonBlockingTest() {
        StepVerifier.create(this.webClient
                .get().uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class)
        ).expectNextMatches(res -> res.getOutput() == 25).verifyComplete();
    }
}
