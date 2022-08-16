package com.formedix.forex.singleton;

import com.formedix.forex.csvReader.CsvReader;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

abstract public class ForexMap {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    CsvReader csvReader = new CsvReader();
    static List<String[]> contents = null;

    private void getContents() {
        if (Objects.isNull(contents)) {
            System.out.println("called here");
            contents = csvReader.getCsvContent("classpath:eurofxref-hist.csv");
        }
        contents.remove(0);
    }

    abstract void initializeMap();

    abstract void populateMap();

    public void execute() {
        getContents();
        initializeMap();
        populateMap();
    }
}
