package com.vinsguru.webfluxdemo.webtestclient;

import static org.mockito.ArgumentMatchers.anyInt;
import static reactor.core.publisher.Mono.just;
import com.vinsguru.webfluxdemo.controller.ReactiveMathValidationController;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * @author debal
 */
@WebFluxTest(ReactiveMathValidationController.class)
public class Lec04ErrorHandlingTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    ReactiveMathService service;

    @Test
    public void errorHandlingTest() {

        Mockito.when(service.findSquare(anyInt())).thenReturn(just(new Response(25)));

        this.client
                //Add '/' for web test client!!
                .get().uri("/reactive-math/square/{input}/throw", 5)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("allowed range is 10-20")
                .jsonPath("$.errorCode").isEqualTo(10)
                .jsonPath("$.input").isEqualTo(5);

    }
}
