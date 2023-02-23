package com.vinsguru.webfluxdemo;

import com.vinsguru.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

/**
 * @author debal
 */
public class Lec05BadRequestTest extends BaseTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void badRequest() {
        StepVerifier.create(this.webClient
                        .get().uri("reactive-math/square/{input}/throw", 5)
                        .retrieve()
                        .bodyToMono(Response.class)
                        .doOnNext(System.out::println)
                        .doOnError(e -> System.out.println(e.getMessage()))
                ).verifyError(WebClientResponseException.BadRequest.class);
    }

}
