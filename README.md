# Documentation

# Setup
The only thing which needs to be done is to replace the API key in
ExchangeServiceImpl:
```
private final String accessKey = "PLACE_YOUR_OWN_KEY";
```

When the project is run it is going to be deployed on localhost:8080

## API Documentation

**Before running any of the requests, keep in mind that the base currency can only be EUR, due to using the free plan of the data provider.**

There are 3 requests which can be called:
### GET /exchange
Retrieves the exchange rate for a given currency pair.

Parameter:
- currencyPair (required): The currency pair for which to retrieve the exchange rate. The expected format is of "xxx/xxx" where xxx is an ISO currency code.
Example: USD/EUR


- Response:
On a successful response, the exchange rate will be returned for the currency pair. On a failure, an error message will be provided.

Example request:
```
http://localhost:8080/exchange?currencyPair=EUR/BGN
```
Example response: 
```
{
    "success": true,
    "exchangeRate": {
        "base": "EUR",
        "quote": "BGN",
        "rate": 1.957542
    }
}
```


### GET /conversion

Perform a currency conversion for a given currency pair and source amount.

Parameters
- currencyPair: The currency pair for which to perform the conversion (e.g., "USD/EUR").
- sourceAmount: The amount to be converted.

Response: On a successful response, the converted amount will be returned along with other details. On a failure, an error message will be provided.

Example request:
```
http://localhost:8080/conversion?currencyPair=EUR/BGN&sourceAmount=10000
```
Example response:
```
{
    "success": true,
    "conversion": {
        "transactionId": 2,
        "transactionDate": "2024-03-05",
        "base": "EUR",
        "quote": "BGN",
        "rate": 1.955724,
        "amount": 19557.24
    }
}
```


### GET /conversionList

Retrieves the conversion history based on transaction ID or transaction date.

Parameters
- transactionId (optional): The ID of the transaction to retrieve.
- transactionDate (optional): The date of the transactions to retrieve.

At least one of the two parameters needs to be provided.

Response: On a successful response,  a list of conversions will be returned based on the provided criteria. On a failure, an error message will be provided.

Example request:
```
http://localhost:8080/conversionList?transactionId=1
```
Example response:
```
{
    "success": true,
    "conversions": [
        {
            "transactionId": 1,
            "transactionDate": "2024-03-05",
            "base": "EUR",
            "quote": "BGN",
            "rate": 1.958499,
            "amount": 19584.99
        }
    ]
}
```

Second Example request:
```
http://localhost:8080/conversionList?transactionDate=2024-03-05
```
Example response:
```
{
    "success": true,
    "conversions": [
        {
            "transactionId": 0,
            "transactionDate": "2024-03-05",
            "base": "EUR",
            "quote": "JPY",
            "rate": 163.019163,
            "amount": 1630191.63
        },
        {
            "transactionId": 1,
            "transactionDate": "2024-03-05",
            "base": "EUR",
            "quote": "BGN",
            "rate": 1.958499,
            "amount": 19584.99
        },
        {
            "transactionId": 2,
            "transactionDate": "2024-03-05",
            "base": "EUR",
            "quote": "BGN",
            "rate": 1.955724,
            "amount": 19557.24
        }
    ]
}
```
