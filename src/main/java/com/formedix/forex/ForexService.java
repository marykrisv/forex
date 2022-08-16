package com.formedix.forex;

import com.formedix.forex.map.CurrencyForexMap;
import com.formedix.forex.map.DateForexMap;
import com.formedix.forex.model.Currency;
import com.formedix.forex.model.ForexCurrency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ForexService {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public List<ForexCurrency> getAllReferenceRate(LocalDate date) {
        List<ForexCurrency> forexCurrencies = new ArrayList<>();
        HashMap<LocalDate, List<ForexCurrency>> map = DateForexMap.getInstance().getMap();

        if (!Objects.isNull(map)) {
            forexCurrencies = map.get(date);
        }

        return forexCurrencies;
    }

    public String convertCurrency(LocalDate date, Currency sourceCurrency, Currency targetCurrency, Double amount) {
        HashMap<LocalDate, List<ForexCurrency>> map = DateForexMap.getInstance().getMap();
        Double price = 0D;

        if (!Objects.isNull(map)) {
            List<ForexCurrency> forexCurrencies = new ArrayList<>();
            forexCurrencies = map.get(date);

            Double sourcePrice = forexCurrencies.get(sourceCurrency.ordinal()).getPrice();
            Double targetPrice = forexCurrencies.get(targetCurrency.ordinal()).getPrice();

            if (sourcePrice != 0D && targetPrice != 0) {
                price = amount / sourcePrice * targetPrice;
            }
        }

        return df.format(price);
    }

    public Double getHighestRate(LocalDate startDate, LocalDate endDate, Currency currency) {
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
            }
        }

        return maxPrice;
    }

    public Double getAverageRate(LocalDate startDate, LocalDate endDate, Currency currency) {
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
            }
        }

        return averageRate;
    }
}
