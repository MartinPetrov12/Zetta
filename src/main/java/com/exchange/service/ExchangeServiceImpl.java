package com.exchange.service;

import com.exchange.database.DatabaseConnection;
import com.exchange.models.*;
import com.exchange.exceptions.InvalidCurrenciesException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ExchangeServiceImpl class.
 *
 * Implements the ExchangeService interface to provide functionality for retrieving exchange rates,
 * performings conversions, and retrieving converion history.
 *
 */

public class ExchangeServiceImpl implements ExchangeService {

    /**
     * WebClient instance for making HTTP requests to the Fixer.io API.
     */
    private WebClient webClient;

    private ObjectMapper objectMapper;

    /**
     * API access key for Fixer.io.
     */
    private final String accessKey = "PLACE_YOUR_OWN_KEY";

    /**
     * Counter for generating unique transaction IDs.
     */
    private int transactionId;

    /**
     * Constructs an ExchangeServiceImpl object with default configuration.
     */
    public ExchangeServiceImpl() {
        this.webClient = WebClient.create("http://data.fixer.io/api");
        this.transactionId = 0;
    }

    /**
     * Retrieves the exchange rate for the given currency pair.
     *
     * @param currencyPair The currency pair, formatted as "base_currency/quote_currency".
     * @return A GetExchangeRateResponse object containing the exchange rate or an error message.
     */
    @Override
    public GetExchangeRateResponse retrieveExchangeRate(String currencyPair) {
        ExchangeRate exchangeRate;
        objectMapper = new ObjectMapper();
        ExchangeResponse response;

        try {
            exchangeRate = new ExchangeRate(currencyPair);
        } catch (Exception e) {
            return new GetExchangeRateResponse(false, e.getMessage());
        }

        try {
            String responseBody = performHttpRequest(exchangeRate.getBase(), exchangeRate.getQuote());
            response = objectMapper.readValue(responseBody, ExchangeResponse.class);
        } catch (JsonProcessingException e) {
            return new GetExchangeRateResponse(false, e.getMessage());
        }

        double rate = response.getRates().get(exchangeRate.getQuote());
        exchangeRate.setRate(rate);
        return new GetExchangeRateResponse(true, exchangeRate);
    }

    /**
     * Performs a currency conversion for the given currency pair and source amount.
     *
     * @param currencyPair The currency pair, formatted as "base_currency/quote_currency".
     * @param sourceAmount The amount to be converted.
     * @return A GetConversionResponse object containing the conversion result or an error message.
     */
    @Override
    public GetConversionResponse retrieveConversion(String currencyPair, double sourceAmount) {
        Conversion result;
        objectMapper = new ObjectMapper();
        ExchangeResponse response;

        try {
            result = new Conversion(currencyPair);
        } catch (InvalidCurrenciesException e) {
            return new GetConversionResponse(false, e.getMessage());
        }

        try {
            String responseBody = performHttpRequest(result.getBase(), result.getQuote());
            response = objectMapper.readValue(responseBody, ExchangeResponse.class);
        } catch (JsonProcessingException e) {
            return new GetConversionResponse(false, e.getMessage());
        }

        double rate = response.getRates().get(result.getQuote());

        result.setRate(rate);
        result.setAmount(sourceAmount * rate);
        result.setTransactionDate(response.getDate());
        result.setTransactionId(transactionId);
        transactionId++;

        saveConversion(result);

        return new GetConversionResponse(true, result);
    }

    /**
     * Retrieves a list of conversions based on transaction ID or transaction date.
     *
     * @param transactionId The ID of the transaction to retrieve (optional).
     * @param transactionDate The date of the transactions to retrieve (optional).
     * @return A GetConversionListResponse object containing the retrieved conversions or an error message.
     */

    @Override
    public GetConversionListResponse retrieveConversionList(Integer transactionId, Date transactionDate){
        if(transactionId == null && transactionDate == null) {
            return new GetConversionListResponse(false, "You need to provide either a transaction id or a transaction date.");
        } else if(transactionId != null) {
            return getConversionById(transactionId);
        } else {
            return getConversionsByDate(transactionDate);
        }
    }

    /**
     * Retrieves all conversions from the provided date.
     *
     * @param transactionDate The date of the transactions to be retrieved.
     * @return A GetConversionListResponse object containing the retrieved conversions or an error message.
     */
    private GetConversionListResponse getConversionsByDate(Date transactionDate) {
        List<Conversion> conversions = new ArrayList<>();
        String sql = "SELECT * FROM Conversion WHERE transactionDate = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, transactionDate);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Conversion conversion = new Conversion();
                    conversion.setTransactionId(resultSet.getInt("transactionId"));
                    conversion.setTransactionDate(resultSet.getDate("transactionDate"));
                    conversion.setBase(resultSet.getString("base"));
                    conversion.setQuote(resultSet.getString("quote"));
                    conversion.setRate(resultSet.getDouble("rate"));
                    conversion.setAmount(resultSet.getDouble("amount"));
                    conversions.add(conversion);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(conversions.isEmpty()) {
            return new GetConversionListResponse(false, "There are no conversions for the date " + transactionDate);
        } else {
            return new GetConversionListResponse(true, conversions);
        }
    }

    /**
     * Retrieves a conversion by the provided id.
     *
     * @param transactionId The id of the transaction to be retrieved.
     * @return A GetConversionListResponse object containing the retrieved conversion or an error message.
     */
    GetConversionListResponse getConversionById(int transactionId) {
        Conversion conversion = null;
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT transactionDate, base, quote, rate, amount FROM Conversion WHERE transactionId = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, transactionId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        Date transactionDate = resultSet.getDate("transactionDate");
                        String base = resultSet.getString("base");
                        String quote = resultSet.getString("quote");
                        double rate = resultSet.getDouble("rate");
                        double amount = resultSet.getDouble("amount");

                        conversion = new Conversion();
                        conversion.setTransactionId(transactionId);
                        conversion.setTransactionDate(transactionDate);
                        conversion.setBase(base);
                        conversion.setQuote(quote);
                        conversion.setRate(rate);
                        conversion.setAmount(amount);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(conversion == null) {
            return new GetConversionListResponse(false, "There isn't a transaction with id: " + transactionId);
        }
        return new GetConversionListResponse(true, List.of(conversion));
    }

    /**
     * Saves a conversion in the h2 database.
     *
     * @param conversion The conversion to be saved.
     */
    void saveConversion(Conversion conversion) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO Conversion (transactionId, transactionDate, base, quote, rate, amount) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, conversion.getTransactionId());
                statement.setDate(2, conversion.getTransactionDate());
                statement.setString(3, conversion.getBase());
                statement.setString(4, conversion.getQuote());
                statement.setDouble(5, conversion.getRate());
                statement.setDouble(6, conversion.getAmount());

                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Performs a http request to the API for the latest rate regarding a currency pair .
     *
     * @param baseCurrency The base currency of the pair.
     * @param quoteCurrency The quote currency of the pair.
     * @return A json response
     */
    String performHttpRequest(String baseCurrency, String quoteCurrency) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/latest")
                        .queryParam("access_key", accessKey)
                        .queryParam("base", baseCurrency)
                        .queryParam("symbols", quoteCurrency)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void setWebClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}


