package com.vinsguru.webfluxdemo.controller;

import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.exception.InputValidationException;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author debal
 */
@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {
    @Autowired
    private ReactiveMathService reactiveMathService;

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input) {
        if (input < 10 || input > 20) {
            throw new InputValidationException(input);
        } else {
            return reactiveMathService.findSquare(input);
        }
    }

    @GetMapping("square/{input}/v2")
    public Mono<ResponseEntity<Response>> findSquareV2(@PathVariable int input) {
        return Mono.just(input)
                .filter(i -> i >=10 && i <=20)
                .flatMap(reactiveMathService::findSquare)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping("square/{input}/mono-error")
    public Mono<Response> monoError(@PathVariable int input) {
        return Mono.just(input)
                .handle((integer, sink) -> {
                            if (integer >= 10 && integer <= 20) {
                                sink.next(integer);
                            } else {
                                sink.error(new InputValidationException(integer));
                            }
                        }).cast(Integer.class).flatMap(i -> reactiveMathService.findSquare(i));
    }
}
