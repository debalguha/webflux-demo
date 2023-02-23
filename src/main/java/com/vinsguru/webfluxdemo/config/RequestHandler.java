package com.vinsguru.webfluxdemo.config;

import static java.lang.Integer.parseInt;
import com.vinsguru.webfluxdemo.dto.MultiplyRequestDto;
import com.vinsguru.webfluxdemo.dto.Response;
import com.vinsguru.webfluxdemo.exception.InputValidationException;
import com.vinsguru.webfluxdemo.service.ReactiveMathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BinaryOperator;

/**
 * @author debal
 */
@Component
public class RequestHandler {
    @Autowired
    private ReactiveMathService reactiveMathService;

    public Mono<ServerResponse> squareHandler(ServerRequest req) {
        return ServerResponse.ok().body(reactiveMathService.findSquare(parseInt(req.pathVariable("input"))), Response.class);
    }

    public Mono<ServerResponse> tableHandler(ServerRequest request) {
        return ServerResponse.ok()
                .body(reactiveMathService.multiplicationTable(parseInt(request.pathVariable("input"))), Response.class);
    }

    public Mono<ServerResponse> tableStreamHandler(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(reactiveMathService.multiplicationTable(parseInt(request.pathVariable("input"))), Response.class);
    }


    public Mono<ServerResponse> multiplyHandler(ServerRequest request) {
        return ServerResponse.ok()
                .body(reactiveMathService.multiply(request.bodyToMono(MultiplyRequestDto.class)), Response.class);
    }

    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest req) {
        final int input = parseInt(req.pathVariable("input"));
        if (input <10 || input >20) {
            return Mono.error(new InputValidationException(input));
        } else {
            return ServerResponse.ok().body(reactiveMathService.findSquare(input), Response.class);
        }
    }

    public Mono<ServerResponse> calculator(ServerRequest req) {
        final int input1 = parseInt(req.pathVariable("input1"));
        final int input2 = parseInt(req.pathVariable("input2"));
        final String operation = req.headers().firstHeader("OP");
        return ServerResponse.ok().bodyValue(Operation.from(operation).calculate(input1, input2));
    }

    enum Operation {
        Add(Integer::sum), Subtract((a, b) -> a -b), Mult((a, b) -> a*b), Div((a, b) -> a/b), Zero((a, b) -> 0);

        BinaryOperator<Integer> fun;
        Operation(BinaryOperator<Integer> fun) {
            this.fun = fun;
        }

        public int calculate(int a, int b) {
            return fun.apply(a, b);
        }

        public static Operation from(String op) {
            switch (op) {
                case "+": return Add;
                case "-": return Subtract;
                case "*": return Mult;
                case "/": return Div;
                default: return Zero;
            }
        }
    }
}
