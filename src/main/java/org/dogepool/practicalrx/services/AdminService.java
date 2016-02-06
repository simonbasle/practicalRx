package org.dogepool.practicalrx.services;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.Month;
import java.util.concurrent.atomic.LongAdder;

import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service for administrative purpose like tracking operational costs.
 */
@Service
public class AdminService {

    private LongAdder currentCosts = new LongAdder();

    public void addCost(int cost) {
        this.currentCosts.add(cost);
    }

    public Observable<BigInteger> costForMonth(int year, Month month) {
        return Observable.just(LocalDate.now())
                .map(now -> {
                    if (year == now.getYear() && month == now.getMonth()) {
                        return BigInteger.valueOf(currentCosts.longValue());
                    }
                    if (year > now.getYear()
                            || year == now.getYear() && month.getValue() > now.getMonthValue()) {
                        return BigInteger.ZERO;
                    }
                    return BigInteger.valueOf(year + month.getValue() * 100);
                });
    }
}
