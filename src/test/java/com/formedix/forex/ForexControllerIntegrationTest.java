package com.formedix.forex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ForexControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    private String VALID_DATE = "2022-03-03";
    private String INVALID_DATE_FORMAT = "2022-03-033";
    private String NO_DATA_DATE = "2024-03-03";

    @Test
    @DisplayName("Given a valid date, then return list of currency")
    void getAllReferenceRateValidDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/forex?date=%s", VALID_DATE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(41)));
    }

    @Test
    @DisplayName("Given an invalid date format, then return bad request status")
    void getAllReferenceRateInvalidDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/forex?date=%s", INVALID_DATE_FORMAT)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given an date with no data, then return not found status")
    void getAllReferenceRateNoDataDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/forex?date=%s", NO_DATA_DATE)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("No currency found in date 2024-03-03")));
    }

    @Test
    @DisplayName("No given date parameter, then return bad request status")
    void getAllReferenceRateNoGivenDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/forex"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Missing date request parameter")));
    }
}
