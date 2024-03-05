package com.exchange.models;

import com.exchange.exceptions.InvalidCurrenciesException;
import com.exchange.utils.CurrencyValidator;

import java.sql.Date;

public class Conversion {

    private int transactionId;
    private Date transactionDate;
    private String base;
    private String quote;
    private Double rate;
    private Double amount;

    public Conversion(String currencyPair) throws InvalidCurrenciesException {
        CurrencyValidator currencyValidator = new CurrencyValidator(currencyPair);
        if(currencyValidator.isSuccessful()) {
            this.setBase(currencyValidator.getBase());
            this.setQuote(currencyValidator.getQuote());
        } else throw new InvalidCurrenciesException();
    }

    public Conversion() {

    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
