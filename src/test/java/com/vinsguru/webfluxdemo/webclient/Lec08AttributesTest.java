package com.vinsguru.webfluxdemo.webclient;

import com.vinsguru.webfluxdemo.dto.MultiplyRequestDto;
import com.vinsguru.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

/**
 * @author debal
 */
public class Lec08AttributesTest extends BaseTest {

    @Autowired
    private WebClient webClient;

    @Test
    public void headersTest() {
        StepVerifier.create(this.webClient
                        .post()
                        .uri("reactive-math/multiply")
                        .bodyValue(multiplyRequestDto(4, 5))
                        .attribute("auth", "oauth")
                        .retrieve()
                        .bodyToMono(Response.class).doOnNext(System.out::println))
                .expectNextMatches(res -> res.getOutput() == 20)
                .verifyComplete();
    }

    private MultiplyRequestDto multiplyRequestDto(int a, int b) {
        return new MultiplyRequestDto(a, b);
    }
}
