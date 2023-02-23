package com.vinsguru.webfluxdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author debal
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MultiplyRequestDto {
    private int first;
    private int second;
}
