package org.dogepool.practicalrx.domain;

public class ExchangeRate {

    public final String moneyCodeFrom;

    public final String moneyCodeTo;

    public final double exchangeRate;

    public ExchangeRate(String moneyCodeFrom, String moneyCodeTo, double exchangeRate) {
        this.moneyCodeFrom = moneyCodeFrom;
        this.moneyCodeTo = moneyCodeTo;
        this.exchangeRate = exchangeRate;
    }
}
