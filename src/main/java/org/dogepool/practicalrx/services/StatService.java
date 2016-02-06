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

    public List<UserStat> getAllStats() {
        List<User> allUsers = userService.findAll();
        int userListSize = allUsers.size();
        List<UserStat> result = new ArrayList<>(userListSize);
        for (User user : allUsers) {
            double hashRateForUser = hashrateService.hashrateFor(user);
            long coins = coinService.totalCoinsMinedBy(user);
            UserStat userStat = new UserStat(user, hashRateForUser, coins);
            result.add(userStat);
        }
        return result;
    }

    public LocalDateTime lastBlockFoundDate() {
        Random rng = new Random(System.currentTimeMillis());
        return LocalDateTime.now().minus(rng.nextInt(72), ChronoUnit.HOURS);
    }

    public User lastBlockFoundBy() {
        Random rng = new Random(System.currentTimeMillis());
        int potentiallyBadIndex = rng.nextInt(10);
        System.out.println("ELECTED: #" + potentiallyBadIndex);

        List<User> allUsers = userService.findAll();
        return allUsers.get(potentiallyBadIndex);
    }
}
