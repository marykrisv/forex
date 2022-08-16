package com.formedix.forex.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Builder(toBuilder = true)
@Getter
@Setter
public class ForexCurrency {
    private LocalDate date;
    private Currency currency;
    private Double price;
}
