package com.formedix.forex;

import com.formedix.forex.model.Currency;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/api/forex")
public class ForexController {
    private final ForexService forexService;

    @GetMapping
    public ResponseEntity<?> getAllReferenceRate(
            @RequestParam @ApiParam(value = "Date (yyyy-MM-dd)", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return ResponseEntity.ok(forexService.getAllReferenceRate(date));
    }

    @GetMapping("/averageExchangeRate")
    public ResponseEntity<?> getAverageRate(
            @RequestParam @ApiParam(value = "Start Date (yyyy-MM-dd)", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam(value = "End Date (yyyy-MM-dd)", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("currency") @ApiParam(value = "Currency") Currency currency
    ) {
        return ResponseEntity.ok(forexService.getAverageRate(startDate, endDate, currency));
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convertCurrency(
            @RequestParam @ApiParam(value = "Date (yyyy-MM-dd)", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam("sourceCurrency") @ApiParam(value = "Source Currency") Currency sourceCurrency,
            @RequestParam("targetCurrency") @ApiParam(value = "Target Currency") Currency targetCurrency,
            @RequestParam Double amount
    ) {
        return ResponseEntity.ok(forexService.convertCurrency(date, sourceCurrency, targetCurrency, amount));
    }

    @GetMapping("/highestExchangeRate")
    public ResponseEntity<?> getHighestRate(
            @RequestParam @ApiParam(value = "Start Date (yyyy-MM-dd)", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam(value = "End Date (yyyy-MM-dd)", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("currency") @ApiParam(value = "Currency") Currency currency
    ) {
        return ResponseEntity.ok(forexService.getHighestRate(startDate, endDate, currency));
    }
}
