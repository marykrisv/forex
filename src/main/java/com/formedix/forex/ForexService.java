package com.formedix.forex;

import com.formedix.forex.exception.ForexCurrencyInvalidCurrencyException;
import com.formedix.forex.exception.ForexCurrencyNotFoundException;
import com.formedix.forex.exception.InvalidDateRangeException;
import com.formedix.forex.map.CurrencyForexMap;
import com.formedix.forex.map.DateForexMap;
import com.formedix.forex.model.Currency;
import com.formedix.forex.model.ForexCurrency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForexService {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<ForexCurrency> getAllReferenceRate(LocalDate date) {
        List<ForexCurrency> forexCurrencies;
        HashMap<LocalDate, List<ForexCurrency>> map = DateForexMap.getInstance().getMap();

        if (!Objects.isNull(map)) {
            forexCurrencies = map.get(date);

            if (Objects.isNull(forexCurrencies) || forexCurrencies.isEmpty()) {
                log.error(String.format("Forex currency list is empty for date %s", date.format(formatter)));
                throw new ForexCurrencyNotFoundException(String.format("No currency found in date %s", date.format(formatter)));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forex Currency CSV content is empty");
        }

        return forexCurrencies;
    }

    public String convertCurrency(LocalDate date, Currency sourceCurrency, Currency targetCurrency, Double amount) {
        HashMap<LocalDate, List<ForexCurrency>> map = DateForexMap.getInstance().getMap();
        Double price = 0D;

        if (!Objects.isNull(map)) {
            List<ForexCurrency> forexCurrencies;
            forexCurrencies = map.get(date);

            if (Objects.isNull(forexCurrencies) || forexCurrencies.isEmpty()) {
                log.error(String.format("Forex currency list is empty for date %s", date.format(formatter)));
                throw new ForexCurrencyNotFoundException(String.format("No currency found in date %s", date.format(formatter)));
            }

            Double sourcePrice = forexCurrencies.get(sourceCurrency.ordinal()).getPrice();
            Double targetPrice = forexCurrencies.get(targetCurrency.ordinal()).getPrice();

            if (sourcePrice == 0D) {
                throw new ForexCurrencyInvalidCurrencyException("Source price is 0");
            } else if (targetPrice == 0) {
                throw new ForexCurrencyInvalidCurrencyException("Target price is 0");
            } else {
                price = amount / sourcePrice * targetPrice;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forex Currency CSV content is empty");
        }

        return df.format(price);
    }

    public Double getHighestRate(LocalDate startDate, LocalDate endDate, Currency currency) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Date Range is invalid");
        }

        HashMap<Currency, List<ForexCurrency>> map = CurrencyForexMap.getInstance().getMap();
        Double maxPrice = 0D;

        if (!Objects.isNull(map)) {
            List<ForexCurrency> forexCurrencies = map.get(currency);

            Optional<Double> maxPriceOptional = forexCurrencies.stream()
                    .filter(item ->
                            item.getDate().isEqual(startDate) || item.getDate().isEqual(endDate) ||
                                    (item.getDate().isAfter(startDate) && item.getDate().isBefore(endDate))
                    )
                    .max(Comparator.comparingDouble(ForexCurrency::getPrice))
                    .map(ForexCurrency::getPrice);

            if (maxPriceOptional.isPresent()) {
                maxPrice = maxPriceOptional.get();
            } else {
                throw new ForexCurrencyNotFoundException(String.format("Currency %s not found", currency.toString()));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forex Currency CSV content is empty");
        }

        return maxPrice;
    }

    public Double getAverageRate(LocalDate startDate, LocalDate endDate, Currency currency) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidDateRangeException("Date Range is invalid");
        }

        HashMap<Currency, List<ForexCurrency>> map = CurrencyForexMap.getInstance().getMap();
        Double averageRate = 0D;

        if (!Objects.isNull(map)) {
            List<ForexCurrency> forexCurrencies = map.get(currency);

            OptionalDouble averagePriceOptional = forexCurrencies.stream()
                    .filter(item ->
                            item.getDate().isEqual(startDate) || item.getDate().isEqual(endDate) ||
                                    (item.getDate().isAfter(startDate) && item.getDate().isBefore(endDate))
                    )
                    .mapToDouble(a -> a.getPrice())
                    .average();

            if (averagePriceOptional.isPresent()) {
                averageRate = averagePriceOptional.getAsDouble();
            } else {
                throw new ForexCurrencyNotFoundException(String.format("Currency %s not found", currency.toString()));
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Forex Currency CSV content is empty");
        }

        return averageRate;
    }
}
