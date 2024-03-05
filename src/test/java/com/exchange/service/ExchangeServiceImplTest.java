package com.exchange.service;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.exchange.models.ExchangeResponse;
import com.exchange.models.GetConversionListResponse;
import com.exchange.models.GetConversionResponse;
import com.exchange.models.GetExchangeRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.mockito.MockitoAnnotations;

import java.util.Map;

public class ExchangeServiceImplTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testRetrieveExchangeRateValidPair() throws Exception {
        // Arrange
        ExchangeServiceImpl exchangeService = spy(new ExchangeServiceImpl());
        String currencyPair = "USD/EUR";
        String responseBody = "{\"rates\":{\"EUR\":0.85},\"base\":\"USD\"}";
        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setRates(Map.of("EUR", 0.85));

        // Mocking performHttpRequest method
        doReturn(responseBody).when(exchangeService).performHttpRequest("USD", "EUR");

        // Mocking ObjectMapper behavior
        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        when(objectMapperMock.readValue(responseBody, ExchangeResponse.class)).thenReturn(exchangeResponse);
        exchangeService.setObjectMapper(objectMapperMock);

        // Act
        GetExchangeRateResponse response = exchangeService.retrieveExchangeRate(currencyPair);

        // Assert
        assertTrue(response.isSuccess());
        assertNotNull(response.getExchangeRate());
        assertEquals(0.85, response.getExchangeRate().getRate(), 0.001);
    }

    @Test
    public void testRetrieveExchangeRateInvalidPair() throws Exception {
        ExchangeServiceImpl exchangeService = new ExchangeServiceImpl();
        String invalidCurrencyPair = "INVALID/PAIR";

        GetExchangeRateResponse response = exchangeService.retrieveExchangeRate(invalidCurrencyPair);

        assertFalse(response.isSuccess());
        assertNotNull(response.getErrorMessage());
    }


    @Test
    public void testRetrieveConversionValidPair() throws Exception {
        // Arrange
        ExchangeServiceImpl exchangeService = spy(new ExchangeServiceImpl());
        String currencyPair = "USD/EUR";
        double sourceAmount = 100.0;
        String responseBody = "{\"rates\":{\"EUR\":0.85},\"base\":\"USD\",\"date\":\"2024-03-05\"}";
        ExchangeResponse exchangeResponse = new ExchangeResponse();
        exchangeResponse.setRates(Map.of("EUR", 0.85));

        doReturn(responseBody).when(exchangeService).performHttpRequest("USD", "EUR");

        ObjectMapper objectMapperMock = mock(ObjectMapper.class);
        when(objectMapperMock.readValue(responseBody, ExchangeResponse.class)).thenReturn(exchangeResponse);
        exchangeService.setObjectMapper(objectMapperMock);

        doNothing().when(exchangeService).saveConversion(any());

        GetConversionResponse response = exchangeService.retrieveConversion(currencyPair, sourceAmount);

        assertTrue(response.isSuccess());
        assertNotNull(response.getConversion());
        assertEquals(85.0, response.getConversion().getAmount(), 0.001);
    }

}
