package com.formedix.forex.map;

import com.formedix.forex.model.Currency;
import com.formedix.forex.model.ForexCurrency;
import lombok.Getter;

import java.time.LocalDate;
import java.util.*;

public class CurrencyForexMap extends ForexMap {
    private static CurrencyForexMap single_instance  = null;

    @Getter
    private HashMap<Currency, List<ForexCurrency>> map;

    private CurrencyForexMap() {
        execute();
    }

    public static CurrencyForexMap getInstance() {
        if (single_instance == null)
            single_instance = new CurrencyForexMap();

        return single_instance;
    }

    @Override
    void initializeMap() {
        map = new HashMap();
    }

    @Override
    void populateMap() {
        contents.stream().forEach((content) -> {
            String[] modifiedContent = Arrays.copyOfRange(content, 1, content.length - 1);
            LocalDate currentDate = LocalDate.parse(content[0], formatter);

            int currencyIndex = 0;
            for (String price: modifiedContent) {
                Currency currentCurrency = Currency.values()[currencyIndex];
                List<ForexCurrency> list = map.get(currentCurrency);
                if (Objects.isNull(list)) {
                    list = new ArrayList<>();
                }
                list.add(ForexCurrency.builder()
                        .date(currentDate)
                        .price((!price.equals("N/A") && !price.equals("")) ? Double.parseDouble(price) : 0)
                        .currency(currentCurrency)
                        .build());
                map.put(currentCurrency, list);

                currencyIndex++;
            }
        });
    }
}
