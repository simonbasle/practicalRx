package org.dogepool.practicalrx.services;

import java.util.Map;

import org.dogepool.practicalrx.error.DogePoolException;
import org.dogepool.practicalrx.error.Error;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * A facade service to get DOGE to USD and DOGE to other currencies exchange rates.
 */
@Service
public class ExchangeRateService {

    @Value("${doge.api.baseUrl}")
    private String dogeUrl;

    @Value("${exchange.free.api.baseUrl}")
    private String exchangeUrl;

    @Autowired
    private RestTemplate restTemplate;

    public Double dogeToCurrencyExchangeRate(String targetCurrencyCode) {
        //get the doge-dollar rate
        double doge2usd = dogeToDollar();

        //get the dollar-currency rate
        double usd2currency = dollarToCurrency(targetCurrencyCode);

        //compute the result
        return doge2usd * usd2currency;
    }

    private double dogeToDollar() {
        try {
            return restTemplate.getForObject(dogeUrl, Double.class);
        } catch (RestClientException e) {
            throw new DogePoolException("Unable to reach doge rate service at " + dogeUrl,
                    Error.UNREACHABLE_SERVICE, HttpStatus.REQUEST_TIMEOUT);
        }
    }

    private double dollarToCurrency(String currencyCode) {
        try {
            Map result = restTemplate.getForObject(exchangeUrl + "/{from}/{to}", Map.class,
                    "USD", currencyCode);
            Double rate = (Double) result.get("exchangeRate");
            if (rate == null)
                rate = (Double) result.get("rate");

            if (rate == null) {
                throw new DogePoolException("Malformed exchange rate", Error.BAD_CURRENCY, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            return rate;
        } catch (HttpStatusCodeException e) {
            throw new DogePoolException("Error processing currency in free API : " + e.getResponseBodyAsString(),
                    Error.BAD_CURRENCY, e.getStatusCode());
        } catch (RestClientException e) {
            throw new DogePoolException("Unable to reach currency exchange service at " + exchangeUrl,
                    Error.UNREACHABLE_SERVICE, HttpStatus.REQUEST_TIMEOUT);
        }
    }
}
