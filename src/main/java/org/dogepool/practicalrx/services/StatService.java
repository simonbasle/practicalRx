package org.dogepool.practicalrx.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to get stats on the pool, like top 10 ladders for various criteria.
 */
@Service
public class StatService {

    @Autowired
    private HashrateService hashrateService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private UserService userService;

    public Observable<UserStat> getAllStats() {
        return userService.findAll()
                .flatMap(u -> {
                    Observable<Double> hr = hashrateService.hashrateFor(u);
                    Observable<Long> co = coinService.totalCoinsMinedBy(u);

                    return Observable.zip(hr, co, (rate, coin) -> new UserStat(u, rate, coin));
                });
    }

    public LocalDateTime lastBlockFoundDate() {
        Random rng = new Random(System.currentTimeMillis());
        return LocalDateTime.now().minus(rng.nextInt(72), ChronoUnit.HOURS);
    }

    public User lastBlockFoundBy() {
        Random rng = new Random(System.currentTimeMillis());
        int potentiallyBadIndex = rng.nextInt(10);
        System.out.println("ELECTED: #" + potentiallyBadIndex);

        List<User> allUsers = userService.findAll().toList().toBlocking().first();
        return allUsers.get(potentiallyBadIndex);
    }
}
