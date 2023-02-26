package com.vinsguru.webfluxdemo.webclient;

import com.vinsguru.webfluxdemo.dto.InputFailedValidationResponse;
import com.vinsguru.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @author debal
 */
public class Lec06BadRequestExchangeTest extends BaseTest {
    @Autowired
    private WebClient webClient;

    @Test
    public void badRequestExchange() {
        StepVerifier.create(this.webClient
                        .get().uri("reactive-math/square/{input}/throw", 5)
                        .exchangeToMono(Lec06BadRequestExchangeTest::exchange)
                        .doOnNext(System.out::println)
                        .doOnError(e -> System.out.println(e.getMessage()))
                //).verifyError(WebClientResponseException.BadRequest.class);
                ).expectNextCount(1).verifyComplete();
    }

    private static Mono<Object> exchange(ClientResponse res) {
        if(res.statusCode() == HttpStatus.BAD_REQUEST) {
            return res.bodyToMono(InputFailedValidationResponse.class);
        } else {
            return res.bodyToMono(Response.class);
        }
    }

}
