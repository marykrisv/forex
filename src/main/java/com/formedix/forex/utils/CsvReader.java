package com.formedix.forex.utils;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CsvReader {
    public List<String[]> getCsvContent(String resourceLocation) {
        List<String[]> contents = null;

        try {
            log.info(String.format("Getting contents from the file %s...", resourceLocation));
            File file = ResourceUtils.getFile(resourceLocation);
            try (CSVReader reader = new CSVReader(new FileReader(file))) {
                contents = reader.readAll();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.error(String.format("File not found %s", resourceLocation));
            } catch (IOException e) {
                e.printStackTrace();
                log.error(String.format("Error occured while reading file %s", resourceLocation));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error(String.format("File not found %s", resourceLocation));
        }

        return contents;
    }
}
