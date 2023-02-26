package com.vinsguru.webfluxdemo.webtestclient;

import static org.mockito.ArgumentMatchers.anyInt;
import static reactor.core.publisher.Mono.just;
import com.vinsguru.webfluxdemo.controller.ParamsController;
import com.vinsguru.webfluxdemo.controller.ReactiveMathController;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * @author debal
 */
@WebFluxTest({ReactiveMathController.class, ParamsController.class})
public class Lec02ControllerGetTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    ReactiveMathService service;

    String queryString = "http://localhost:8080/jobs/search?count={count}&page={page}";
    @Test
    void singleResponseTest() {

        Mockito.when(service.findSquare(anyInt())).thenReturn(just(new Response(25)));

        this.webTestClient
                //Add '/' for web test client!!
                .get().uri("/reactive-math/square/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));

    }

    @Test
    void listResponseTest() {

        Mockito.when(service.multiplicationTable(5)).
                thenReturn(Flux.range(1, 5).map(Response::new));

        this.webTestClient
                //Add '/' for web test client!!
                .get().uri("/reactive-math/table/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Response.class)
                .hasSize(5);

    }

    @Test
    void streamingResponseTest() {
        final Flux<Response> responseFlux = Flux.range(1, 5)
                .delayElements(Duration.ofMillis(100))
                .map(Response::new);
        Mockito.when(service.multiplicationTable(5))
                .thenReturn(responseFlux);

        this.webTestClient
                //Add '/' for web test client!!
                .get().uri("/reactive-math/table/{input}/stream", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_EVENT_STREAM)
                .expectBodyList(Response.class)
                .hasSize(5);

    }

    @Test
    public void queryParamsTest1() {
        this.webTestClient.get()
                .uri(b -> b.path("/jobs/search").query("count={count}&page={page}").build(4, 5))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(2).contains(4, 5);
    }

    @Test
    public void postTest() {
        this.webTestClient.get()
                .uri(b -> b.path("/jobs/search").query("count={count}&page={page}").build(4, 5))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(Integer.class)
                .hasSize(2).contains(4, 5);
    }

}
