package com.formedix.forex;

import com.formedix.forex.model.Currency;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forex")
public class ForexController {
    private final ForexService forexService;

    @GetMapping
    public ResponseEntity<?> getAllReferenceRate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(forexService.getAllReferenceRate(date));
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("sourceCurrency") Currency sourceCurrency,
            @RequestParam("targetCurrency") Currency targetCurrency,
            @RequestParam Double amount
    ) {
        return ResponseEntity.ok(forexService.convertCurrency(date, sourceCurrency, targetCurrency, amount));
    }

    @GetMapping("/highestExchangeRate")
    public ResponseEntity<?> getHighestRate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("currency") Currency currency
    ) {
        return ResponseEntity.ok(forexService.getHighestRate(startDate, endDate, currency));
    }

    @GetMapping("/averageExchangeRate")
    public ResponseEntity<?> getAverageRate(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("currency") Currency currency
    ) {
        return ResponseEntity.ok(forexService.getAverageRate(startDate, endDate, currency));
    }
}
