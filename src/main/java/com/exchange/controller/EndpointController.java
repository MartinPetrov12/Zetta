package com.exchange.controller;

import com.exchange.models.*;
import com.exchange.service.ExchangeService;
import com.exchange.service.ExchangeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController("")
public class EndpointController {

    private ExchangeService exchangeService;

    @Autowired
    public EndpointController() {
        this.exchangeService = new ExchangeServiceImpl();
    }

    @GetMapping("/exchange")
    public GetExchangeRateResponse getExchangeRate(@RequestParam String currencyPair) {
        return exchangeService.retrieveExchangeRate(currencyPair);
    }

    @GetMapping("/conversion")
    public GetConversionResponse getConversion(@RequestParam String currencyPair, @RequestParam double sourceAmount) {
        return exchangeService.retrieveConversion(currencyPair, sourceAmount);
    }

    @GetMapping("/conversionList")
    public GetConversionListResponse getConversionList(@RequestParam(required = false) Integer transactionId, @RequestParam(required = false)  Date transactionDate) {
        return exchangeService.retrieveConversionList(transactionId, transactionDate);
    }

}
