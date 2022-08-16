package com.formedix.forex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ForexController {
    @GetMapping
    public String getAllReferenceRate() {
        return "Hello World";
    }
}
