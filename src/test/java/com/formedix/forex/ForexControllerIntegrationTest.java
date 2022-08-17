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

    private String VALID_DATE = "2020-01-31";
    private String INVALID_DATE_FORMAT = "2022-03-033";
    private String NO_DATA_DATE = "2024-03-03";
    private String VALID_START_DATE = "2022-03-03";
    private String VALID_END_DATE = "2022-08-03";
    private String VALID_CURRENCY = "USD";
    private String VALID_START_DATE_NO_DATA_IN_DATE_RANGE = "2023-03-03";
    private String VALID_END_DATE_NO_DATA_IN_DATE_RANGE = "2023-03-04";
    private String VALID_CURRENCY_NO_DATA_IN_DATE_RANGE = "CYP";
    private String INVALID_CURRENCY = "UUU";
    private String SOURCE_CURRENCY = "USD";
    private String TARGET_CURRENCY = "JPY";
    private String VALID_AMOUNT = "10";
    private String INVALID_AMOUNT = "10s";
    private String ZERO_CURRENCY = "CYP";

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
    @DisplayName("Given a date with no data, then return not found status")
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
    @DisplayName("Given an invalid currency format, then return bad request status")
    void getAverageRate_InvalidCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/averageExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        INVALID_CURRENCY,
                        VALID_END_DATE,
                        VALID_START_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given an invalid date range, then return bad request status")
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
    @DisplayName("Given an invalid date range, then return not found status")
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
    @DisplayName("Given a valid date, source currency, target currency and valid amount; then return the converted rate")
    void convertCurrency_ValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/convert?date=%s&sourceCurrency=%s&targetCurrency=%s&amount=%s",
                        VALID_DATE,
                        SOURCE_CURRENCY,
                        TARGET_CURRENCY,
                        VALID_AMOUNT)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1088.94)));
    }

    @Test
    @DisplayName("Given an invalid amount, then return bad request status")
    void convertCurrency_InvalidAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/convert?date=%s&sourceCurrency=%s&targetCurrency=%s&amount=%s",
                        VALID_DATE,
                        SOURCE_CURRENCY,
                        TARGET_CURRENCY,
                        INVALID_AMOUNT)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given a source currency with 0 price, then return bad request status")
    void convertCurrency_ZeroSourceCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/convert?date=%s&sourceCurrency=%s&targetCurrency=%s&amount=%s",
                        VALID_DATE,
                        ZERO_CURRENCY,
                        TARGET_CURRENCY,
                        VALID_AMOUNT)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Source price is 0")));
    }

    @Test
    @DisplayName("Given a target currency with 0 price, then return bad request status")
    void convertCurrency_ZeroTargetCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/convert?date=%s&sourceCurrency=%s&targetCurrency=%s&amount=%s",
                        VALID_DATE,
                        SOURCE_CURRENCY,
                        ZERO_CURRENCY,
                        VALID_AMOUNT)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Target price is 0")));
    }

    @Test
    @DisplayName("No given amount parameter, then return bad request status")
    void convertCurrency_NoGivenAmount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/convert?date=%s&sourceCurrency=%s&targetCurrency=%s&",
                        VALID_DATE,
                        SOURCE_CURRENCY,
                        ZERO_CURRENCY)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Missing amount request parameter")));
    }

    @Test
    @DisplayName("Given a valid currency, start date, and end date; then return the highest exchange rate")
    void getHighestRate_ValidData() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/highestExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        VALID_CURRENCY,
                        VALID_START_DATE,
                        VALID_END_DATE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1.1126D)));
    }

    @Test
    @DisplayName("Given an invalid currency format, then return bad request status")
    void getHighestRate_InvalidCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/highestExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        INVALID_CURRENCY,
                        VALID_START_DATE,
                        VALID_END_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Given an invalid date range, then return bad request status")
    void getHighestRate_InvalidDateRange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/highestExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        VALID_CURRENCY,
                        VALID_END_DATE,
                        VALID_START_DATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Date Range is invalid")));
    }

    @Test
    @DisplayName("Given an invalid date range, then return not found status")
    void getHighestRate_CurrencyNoDataInDateRange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/highestExchangeRate?currency=%s&startDate=%s&endDate=%s",
                        VALID_CURRENCY_NO_DATA_IN_DATE_RANGE,
                        VALID_START_DATE_NO_DATA_IN_DATE_RANGE,
                        VALID_END_DATE_NO_DATA_IN_DATE_RANGE)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Currency CYP not found")));
    }

    @Test
    @DisplayName("No given endDate parameter, then return bad request status")
    void getHighestRate_NoGivenCurrency() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format(
                        "/api/forex/highestExchangeRate?currency=%s&startDate=%s",
                        VALID_CURRENCY,
                        VALID_START_DATE)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Missing endDate request parameter")));
    }
}
