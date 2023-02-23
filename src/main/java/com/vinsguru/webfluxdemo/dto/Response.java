package com.vinsguru.webfluxdemo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @author debal
 */
@Data
@NoArgsConstructor
@ToString
public class Response {
    private Date date = new Date();
    private int output;

    public Response(int output) {
        this.output = output;
    }
}
