package org.dogepool.practicalrx.controllers;

import org.dogepool.practicalrx.domain.ExchangeRate;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(value = "/rate", produces = MediaType.APPLICATION_JSON_VALUE)
public class RateController {

    @Autowired
    private ExchangeRateService service;

    @RequestMapping("{moneyTo}")
    public DeferredResult rate(@PathVariable String moneyTo) {
        DeferredResult<ExchangeRate> result = new DeferredResult<>();
        service.dogeToCurrencyExchangeRate(moneyTo)
                .map(rate -> new ExchangeRate("DOGE", moneyTo, rate))
                .subscribe(exchangeRate -> result.setResult(exchangeRate),
                        error -> result.setErrorResult(error));
        return result;
    }
}
