package com.exchange.service;

import com.exchange.models.*;

import java.sql.Date;

/**
 * ExchangeService interface.
 *
 * Defines methods for retrieving exchange rates, performing conversions, and retrieving conversion history.
 *
 */
public interface ExchangeService {

    /**
     * Retrieves the exchange rate for the given currency pair.
     *
     * @param currencyPair The currency pair, formatted as "base_currency/quote_currency".
     * @return A GetExchangeRateResponse object containing the exchange rate or an error message.
     */
    GetExchangeRateResponse retrieveExchangeRate(String currencyPair);

    /**
     * Performs a currency conversion for the given currency pair and source amount.
     *
     * @param currencyPair The currency pair, formatted as "base_currency/quote_currency".
     * @param sourceAmount The amount to be converted.
     * @return A GetConversionResponse object containing the conversion result or an error message.
     */
    GetConversionResponse retrieveConversion(String currencyPair, double sourceAmount);

    /**
     * Retrieves a list of conversions based on transaction ID or transaction date.
     *
     * @param transactionId The ID of the transaction to retrieve (optional).
     * @param transactionDate The date of the transactions to retrieve (optional).
     * @return A GetConversionListResponse object containing the retrieved conversions or an error message.
     */
    GetConversionListResponse retrieveConversionList(Integer transactionId, Date transactionDate);
}
