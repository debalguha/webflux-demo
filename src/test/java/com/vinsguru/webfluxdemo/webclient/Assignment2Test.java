package com.vinsguru.webfluxdemo.webclient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

/**
 * @author debal
 */
public class Assignment2Test extends BaseTest {
    @Autowired
    private WebClient webClient;

    final static String FORMAT = "%d %s %d = %s";
    final static List<String> ops = List.of("+", "-", "*", "/");
    final static int INPUT1 = 10;
    final static List<Integer> Input2s = List.of(1,2,3,4,5);

    @Test
    public void assignmentInvokeCalculatorForAllOpsAndForAllInput2() {
        final Flux<String> stringFlux = Flux.range(1, 5)
                .flatMap(b -> Flux.fromIterable(ops).flatMap(op -> request(b, op)))
                .doOnNext(System.out::println);

        StepVerifier.create(stringFlux)
                .expectNextCount(20)
                .verifyComplete();
    }

    private Mono<String> request(int input2, String op) {
        return this.webClient.get().uri("router/calc/{input1}/{input2}", INPUT1, input2)
                .header("OP", op)
                .retrieve()
                .bodyToMono(String.class)
                .map(s -> String.format(FORMAT, INPUT1, op, input2, s));
    }
}
