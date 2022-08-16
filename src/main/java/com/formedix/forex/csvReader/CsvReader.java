package com.formedix.forex.csvReader;

import com.opencsv.CSVReader;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
    public List<String[]> getCsvContent(String resourceLocation) {
        List<String[]> contents = null;
        try {
            File file = ResourceUtils.getFile(resourceLocation);
            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                contents = reader.readAll();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return contents;
    }
}
