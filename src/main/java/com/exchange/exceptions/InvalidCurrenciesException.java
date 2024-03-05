package com.exchange.exceptions;

public class InvalidCurrenciesException extends Exception {
    public InvalidCurrenciesException() {
        super("The currency pair you have provided is invalid or not supported by the API.");
    }
}
