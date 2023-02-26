package com.vinsguru.webfluxdemo.webtestclient;

import com.vinsguru.webfluxdemo.controller.ReactiveMathController;
import com.vinsguru.webfluxdemo.dto.MultiplyRequestDto;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author debal
 */
@WebFluxTest(ReactiveMathController.class)
public class Lec03ControllerPostTest {
    @Autowired
    private WebTestClient client;

    @MockBean
    ReactiveMathService reactiveMathService;

    @Test
    public void posTest() {
        Mockito.when(reactiveMathService.multiply(Mockito.any())).
                thenReturn(Mono.just(new Response(1)));

        this.client
                //Add '/' for web test client!!
                .post().uri("/reactive-math/multiply", 5)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new MultiplyRequestDto())
                .exchange()
                .expectStatus().is2xxSuccessful();
    }
}
