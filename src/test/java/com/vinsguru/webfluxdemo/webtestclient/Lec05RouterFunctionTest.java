package com.vinsguru.webfluxdemo.webtestclient;

import com.vinsguru.webfluxdemo.config.RequestHandler;
import com.vinsguru.webfluxdemo.config.RouterConfig;
import com.vinsguru.webfluxdemo.dto.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author debal
 */
@WebFluxTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ContextConfiguration(classes = RouterConfig.class)
public class Lec05RouterFunctionTest {
    private WebTestClient client;

    @Autowired
    ApplicationContext ctx;
    //RouterConfig routerConfig;

    @MockBean
    RequestHandler handler;

/*    @BeforeAll
    public void setClient() {
        client = WebTestClient.bindToRouterFunction(routerConfig.router()).build();
    }*/

        @BeforeAll
    public void setClient() {
        client = WebTestClient.bindToApplicationContext(ctx).build();
    }

    @Test
    public void testSquare() {
        Mockito.when(handler.squareHandler(Mockito.any(ServerRequest.class))).thenReturn(ServerResponse.ok().bodyValue(new Response(25)));
        client.get()
                .uri("/router/square/{input}", 5)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Response.class)
                .value(r -> Assertions.assertThat(r.getOutput()).isEqualTo(25));
    }
}
