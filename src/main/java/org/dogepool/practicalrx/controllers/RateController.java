package org.dogepool.practicalrx.controllers;

import org.dogepool.practicalrx.domain.ExchangeRate;
import org.dogepool.practicalrx.error.*;
import org.dogepool.practicalrx.error.Error;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rate", produces = MediaType.APPLICATION_JSON_VALUE)
public class RateController {

    @Autowired
    private ExchangeRateService service;

    @RequestMapping("{moneyTo}")
    public ExchangeRate rate(@PathVariable String moneyTo) {
            Double exchange = service.dogeToCurrencyExchangeRate(moneyTo);
        if (exchange == null) {
            throw new DogePoolException("Cannot find rate for " + moneyTo, Error.BAD_CURRENCY, HttpStatus.NOT_FOUND);
        }
        return new ExchangeRate("DOGE", moneyTo, exchange);
    }
}
