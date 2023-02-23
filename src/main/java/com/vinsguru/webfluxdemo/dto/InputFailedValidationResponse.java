package com.vinsguru.webfluxdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author debal
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InputFailedValidationResponse {
    private int errorCode;
    private int input;
    private String message;
}
