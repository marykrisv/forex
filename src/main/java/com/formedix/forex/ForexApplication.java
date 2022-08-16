package com.formedix.forex;

import com.formedix.forex.model.Currency;
import com.formedix.forex.model.ForexCurrency;
import com.formedix.forex.singleton.CurrencyForexMap;
import com.formedix.forex.singleton.DateForexMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@SpringBootApplication
public class ForexApplication {

    public static void main(String[] args) {
        SpringApplication.run(ForexApplication.class, args);
        HashMap<Currency, List<ForexCurrency>> map = CurrencyForexMap.getInstance().getMap();
        HashMap<LocalDate, List<ForexCurrency>> dateListHashMap = DateForexMap.getInstance().getMap();
        System.out.println("hey");
    }

}
