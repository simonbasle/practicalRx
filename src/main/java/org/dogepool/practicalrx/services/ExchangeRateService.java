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
import rx.Observable;

/**
 * A facade service to get DOGE to USD and DOGE to other currencies exchange rates.
 */
@Service
public class ExchangeRateService {

    @Value("${doge.api.baseUrl}")
    private String dogeUrl;

    @Value("${exchange.free.api.baseUrl}")
    private String exchangeUrl;

    @Value("${exchange.nonfree.api.baseUrl}")
    private String exchangeNonfreeUrl;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RestTemplate restTemplate;

    public Observable<Double> dogeToCurrencyExchangeRate(String targetCurrencyCode) {
        Observable<Double> dollarToCurrency =
                dollarToCurrency(targetCurrencyCode)
                .doOnError(e -> System.out.println("FALLING BACK TO NON-FREE EXCHANGE RATE SERVICE"))
                .onErrorResumeNext(t -> (t instanceof DogePoolException)
                        ? dollarToCurrencyPaid(targetCurrencyCode)
                        : Observable.error(t));

        return dogeToDollar()
                .zipWith(dollarToCurrency, (doge2usd, usd2currency) -> doge2usd * usd2currency);
    }

    private Observable<Double> dogeToDollar() {
        return Observable.create(sub -> {
            try {
                Double rate = restTemplate.getForObject(dogeUrl, Double.class);
                sub.onNext(rate);
                sub.onCompleted();
            } catch (RestClientException e) {
                sub.onError(new DogePoolException("Unable to reach doge rate service at " + dogeUrl,
                        Error.UNREACHABLE_SERVICE, HttpStatus.REQUEST_TIMEOUT));
            } catch (Exception e) {
                sub.onError(e);
            }
        });
    }

    private Observable<Double> dollarToCurrency(String currencyCode) {
        return Observable.<Double>create(sub -> {
            try {
                Map result = restTemplate.getForObject(exchangeUrl + "/{from}/{to}", Map.class,
                        "USD", currencyCode);
                Double rate = (Double) result.get("exchangeRate");
                if (rate == null)
                    rate = (Double) result.get("rate");

                if (rate == null) {
                    sub.onError(new DogePoolException("Malformed exchange rate", Error.BAD_CURRENCY, HttpStatus.UNPROCESSABLE_ENTITY));
                }
                sub.onNext(rate);
                sub.onCompleted();
            } catch (HttpStatusCodeException e) {
                sub.onError(new DogePoolException("Error processing currency in free API : " + e.getResponseBodyAsString(),
                        Error.BAD_CURRENCY, e.getStatusCode()));
            } catch (RestClientException e) {
                sub.onError(new DogePoolException("Unable to reach free currency exchange service at " + exchangeUrl,
                        Error.UNREACHABLE_SERVICE, HttpStatus.REQUEST_TIMEOUT));
            } catch (Exception e) {
                sub.onError(e);
            }
        });
    }

    private Observable<Double> dollarToCurrencyPaid(String currencyCode) {
        return Observable.<Double>create(sub -> {
            try {
                Map result = restTemplate.getForObject(exchangeNonfreeUrl + "/{from}/{to}", Map.class,
                        "USD", currencyCode);
                Double rate = (Double) result.get("exchangeRate");
                if (rate == null)
                    rate = (Double) result.get("rate");

                if (rate == null) {
                    sub.onError(new DogePoolException("Malformed exchange rate from paid service",
                            Error.BAD_CURRENCY, HttpStatus.UNPROCESSABLE_ENTITY));
                }
                sub.onNext(rate);
                sub.onCompleted();
            } catch (HttpStatusCodeException e) {
                sub.onError(new DogePoolException("Error processing currency in paid API : " + e.getResponseBodyAsString(),
                        Error.BAD_CURRENCY, e.getStatusCode()));
            } catch (RestClientException e) {
                sub.onError(new DogePoolException("Unable to reach paid currency exchange service at " + exchangeUrl,
                        Error.UNREACHABLE_SERVICE, HttpStatus.REQUEST_TIMEOUT));
            }
        })
        .doOnNext(r -> adminService.addCost(1))
        .doOnCompleted(() -> System.out.println("CALLED PAID EXCHANGE RATE SERVICE FOR 1$"));
    }
}
