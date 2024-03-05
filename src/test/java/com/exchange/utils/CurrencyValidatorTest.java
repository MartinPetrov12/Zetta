package com.exchange.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyValidatorTest {
    @Test
    void CorrectCurrencyValidatorSetUp() {
        CurrencyValidator validCurrencyValidator = new CurrencyValidator("EUR/USD");
        assertTrue(validCurrencyValidator.isSuccessful());
    }

    @Test
    void IncorrectCurrencyValidatorSetUp1() {
        CurrencyValidator validCurrencyValidator = new CurrencyValidator("EUR/ABC");
        assertFalse(validCurrencyValidator.isSuccessful());
    }

    @Test
    void IncorrectCurrencyValidatorSetUp2() {
        CurrencyValidator validCurrencyValidator = new CurrencyValidator("EUR/USD/JPY");
        assertFalse(validCurrencyValidator.isSuccessful());
    }

}