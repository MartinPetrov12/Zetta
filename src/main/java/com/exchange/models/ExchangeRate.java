package com.exchange.models;

import com.exchange.exceptions.InvalidCurrenciesException;
import com.exchange.utils.CurrencyValidator;

public class ExchangeRate {
    private String base;
    private String quote;
    private Double rate;

    public ExchangeRate(String currencyPair) throws InvalidCurrenciesException {
        CurrencyValidator currencyValidator = new CurrencyValidator(currencyPair);
        if(currencyValidator.isSuccessful()) {
            this.setBase(currencyValidator.getBase());
            this.setQuote(currencyValidator.getQuote());
        } else throw new InvalidCurrenciesException();
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
