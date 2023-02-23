package com.vinsguru.webfluxdemo.service;

import static java.util.stream.Collectors.toList;
import com.vinsguru.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author debal
 */
@Service
public class MathService {
    public Response findSquare(int input) {
        return new Response(input * input);
    }

    public List<Response> multiplicationTable(int input) {
        return IntStream.range(1, 10)
                .peek(SleepUtil::sleepSeconds)
                .peek(i -> System.out.println("math-service processing element "+i))
                .mapToObj(i -> new Response(i * input))
                .collect(toList());
    }
}
