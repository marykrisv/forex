package com.formedix.forex.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder(toBuilder = true)
public class ForexCurrency {
    private LocalDate date;
    private Currency currency;
    private Double price;
}
