package org.dogepool.practicalrx.services;

import java.util.Collections;
import java.util.List;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * Service to get ladders and find a user's rankings in the pool.
 */
@Service
public class RankingService {

    @Autowired
    private StatService statService;

    /**
     * Find the user's rank by hashrate in the pool. This is a costly operation.
     * @return the rank of the user in terms of hashrate. If it couldnt' be established it'll be ranked last.
     */
    public Observable<Integer> rankByHashrate(User user) {
        return rankByHashrate()
                .takeUntil(stat -> stat.user.equals(user))
                .count();
    }

    /**
     * Find the user's rank by number of coins found. This is a costly operation.
     * @return the rank of the user in terms of coins found. If user is not found, it will be ranked last.
     */
    public Observable<Integer> rankByCoins(User user) {
        return rankByCoins()
                .takeUntil(userStat -> user.equals(userStat.user))
                .count();
    }

    public Observable<UserStat> getLadderByHashrate() {
        return rankByHashrate()
                .take(10);
    }

    public Observable<UserStat> getLadderByCoins() {
        return rankByCoins()
                .take(10);
    }

    protected Observable<UserStat> rankByHashrate() {
        List<UserStat> allStats = statService.getAllStats().toList().toBlocking().single();
        Collections.sort(allStats, (o1, o2) -> {
            double h1 = o1.hashrate;
            double h2 = o2.hashrate;
            double diff = h2 - h1;
            if (diff == 0d) {
                return 0;
            } else {
                return diff > 0d ? 1 : -1;
            }
        });
        return Observable.from(allStats);
    }

    protected Observable<UserStat> rankByCoins() {
        List<UserStat> allStats = statService.getAllStats().toList().toBlocking().single();
        Collections.sort(allStats, (o1, o2) -> {
            long c1 = o1.totalCoinsMined;
            long c2 = o2.totalCoinsMined;
            long diff = c2 - c1;
            if (diff == 0L) {
                return 0;
            } else {
                return diff > 0L ? 1 : -1;
            }
        });
        return Observable.from(allStats);
    }
}
