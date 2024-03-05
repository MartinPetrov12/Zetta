package com.exchange.utils;

/**
 * CurrencyValidator class.
 *
 * Validates a given currency pair by checking its format and verifying if both currencies are valid.
 *
 */
public class CurrencyValidator {

    /**
     * Flag indicating validation success.
     */
    private boolean success;
    private String base;
    private String quote;

    /**
     * Constructs a CurrencyValidator object for the given currency pair.
     *
     * @param currencyPair The currency pair to be validated, formatted as "base_currency/quote_currency".
     */
    public CurrencyValidator(String currencyPair) {
        String[] currencies = currencyPair.split("/");
        if(currencies.length != 2) {
            setSuccess(false);
        } else if(currencies[0].equals(currencies[1])) {
            setSuccess(false);
        } else {
            boolean check1 = false;
            boolean check2 = false;
            for(VALID_CURRENCIES currency: VALID_CURRENCIES.values()) {
                if(currency.toString().equals(currencies[0])) {
                    check1 = true;
                }
                if(currency.toString().equals(currencies[1])) {
                    check2 = true;
                }
            }
            if(!check1 || !check2) {
                setSuccess(false);
            } else {
                setBase(currencies[0]);
                setQuote(currencies[1]);
                setSuccess(true);
            }
        }
    }

    public boolean isSuccessful() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
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
}
