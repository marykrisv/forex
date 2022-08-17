package com.formedix.forex.map;

import com.formedix.forex.utils.CsvReader;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

abstract public class ForexMap {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    CsvReader csvReader = new CsvReader();
    static List<String[]> contents = null;

    private void getContents() {
        if (Objects.isNull(contents)) {
            contents = csvReader.getCsvContent("classpath:eurofxref-hist.csv");
        }
        removeHeader();
    }

    private void removeHeader() {
        if (!Objects.isNull(contents)) {
            contents.remove(0);
        }
    }

    abstract void initializeMap();

    abstract void populateMap();

    public void execute() {
        getContents();
        initializeMap();
        populateMap();
    }
}
