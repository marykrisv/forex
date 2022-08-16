package com.formedix.forex.map;

import com.formedix.forex.model.Currency;
import com.formedix.forex.model.ForexCurrency;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DateForexMap extends ForexMap {
    private static DateForexMap single_instance  = null;

    @Getter
    private HashMap<LocalDate, List<ForexCurrency>> map;

    private DateForexMap() {
        execute();
    }

    public static DateForexMap getInstance() {
        if (single_instance == null)
            single_instance = new DateForexMap();

        return single_instance;
    }

    @Override
    void initializeMap() {
        map = new HashMap();
    }

    @Override
    void populateMap() {
        contents.stream().forEach((content) -> {
            String[] modifiedContent = Arrays.copyOfRange(content, 1, content.length);
            LocalDate currentDate = LocalDate.parse(content[0], formatter);

            AtomicInteger currencyIndex = new AtomicInteger();
            List<ForexCurrency> list = Arrays.stream(modifiedContent)
                    .map((price) -> {
                        ForexCurrency forexCurrency = null;
                        if (!price.equals("N/A") && !price.equals("")) {
                            Currency currentCurrency = Currency.values()[currencyIndex.get()];

                            forexCurrency = ForexCurrency.builder()
                                    .currency(currentCurrency)
                                    .price(Double.parseDouble(price))
                                    .build();
                        }
                        currencyIndex.getAndIncrement();
                        return forexCurrency;
                    })
                    .filter(forexCurrency -> !Objects.isNull(forexCurrency))
                    .collect(Collectors.toList());

            map.put(currentDate, list);
        });
    }
}
