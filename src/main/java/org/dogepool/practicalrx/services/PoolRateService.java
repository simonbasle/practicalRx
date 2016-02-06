package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to retrieve the current global hashrate for the pool.
 */
@Service
public class PoolRateService {

    @Autowired
    private PoolService poolService;

    @Autowired
    private HashrateService hashrateService;

    public double poolGigaHashrate() {
        double hashrate = 0d;
        for (User u : poolService.miningUsers()) {
            double userRate = hashrateService.hashrateFor(u);
            hashrate += userRate;
        }
        return hashrate;
    }
}
