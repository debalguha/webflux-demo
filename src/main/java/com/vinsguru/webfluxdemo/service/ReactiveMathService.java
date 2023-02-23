package com.vinsguru.webfluxdemo.service;

import com.vinsguru.webfluxdemo.dto.MultiplyRequestDto;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.exception.InputValidationException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author debal
 */
@Service
public class ReactiveMathService {
    public Mono<Response> findSquare(int input) {
        return Mono.fromSupplier(() -> input * input)
                .map(Response::new);
    }

    public Flux<Response> multiplicationTable(int input) {
        return Flux.range(1, 10)
                .doOnNext(SleepUtil::sleepSeconds)
                .doOnNext(i -> System.out.println("math-service processing element "+i))
                .map(i -> i * input)
                .map(Response::new);

    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono) {
        return dtoMono
                .map(dto -> dto.getFirst() * dto.getSecond())
                .map(Response::new);
    }
}
