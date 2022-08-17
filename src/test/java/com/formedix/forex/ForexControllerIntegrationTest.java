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
    private String VALID_START_DATE = "2022-03-03";
    private String VALID_END_DATE = "2022-08-03";
    private String VALID_CURRENCY = "USD";
    private String VALID_START_DATE_NO_DATA_IN_DATE_RANGE = "2023-03-03";
    private String VALID_END_DATE_NO_DATA_IN_DATE_RANGE = "2023-03-04";
    private String VALID_CURRENCY_NO_DATA_IN_DATE_RANGE = "CYP";
    private String INVALID_CURRENCY = "UUU";

    @Test
    @DisplayName("Given a valid date, then return list of currency")
    void getAllReferenceRate_ValidDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/api/forex?date=%s", VALID_DATE)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(41)));
    }

    @Test
    @DisplayName("Given an invalid date format, then return bad request status")
    void getAllReferenceRate_InvalidDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/forex?date=%s", INVALID_DATE_FORMAT)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given an date with no data, then return not found status")
    void getAllReferenceRate_NoDataDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(String.format("/api/forex?date=%s", NO_DATA_DATE)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("No currency found in date 2024-03-03")));
    }

    @Test
    @DisplayName("No given date parameter, then return bad request status")
    void getAllReferenceRate_NoGivenDate() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/forex"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Missing date request parameter")));
    }

    @Test
    @DisplayName("Given a valid currency, start date, and end date; then return the average exchange rate")
    void getAverageRate_ValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                "/api/forex/averageExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        VALID_CURRENCY,
                        VALID_START_DATE,
                        VALID_END_DATE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1.0613953703703705D)));
    }

    @Test
    @DisplayName("Given an invalid date range; then return bad request status")
    void getAverageRate_InvalidDateRange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/averageExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        VALID_CURRENCY,
                        VALID_END_DATE,
                        VALID_START_DATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Date Range is invalid")));
    }

    @Test
    @DisplayName("Given an invalid date range; then return not found status")
    void getAverageRate_CurrencyNoDataInDateRange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/averageExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        VALID_CURRENCY_NO_DATA_IN_DATE_RANGE,
                        VALID_START_DATE_NO_DATA_IN_DATE_RANGE,
                        VALID_END_DATE_NO_DATA_IN_DATE_RANGE)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Currency CYP not found")));
    }

    @Test
    @DisplayName("No given currency parameter, then return bad request status")
    void getAverageRate_NoGivenCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/averageExchangeRate?startDate=%s&endDate=%s",
                        VALID_END_DATE,
                        VALID_START_DATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Missing currency request parameter")));
    }

    @Test
    @DisplayName("Given an invalid currency format, then return bad request status")
    void getAverageRate_InvalidCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/averageExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        INVALID_CURRENCY,
                        VALID_END_DATE,
                        VALID_START_DATE)))
                .andExpect(status().isBadRequest());
    }
}
