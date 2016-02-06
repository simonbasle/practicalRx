package org.dogepool.practicalrx.services;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

/**
 * A service to search for users by name, login and other criteria.
 */
@Service
public class SearchService {

    @Autowired
    private UserService userService;

    @Autowired
    private CoinService coinService;

    /**
     * Find users whose display name contain the given namePattern.
     *
     * @param namePattern the string to find in a user's displayName for him to match (ignoring case).
     * @return the stream of matching users.
     */
    public Observable<User> findByName(String namePattern) {
        String upperPattern = namePattern.toUpperCase();
        return userService.findAll()
                .filter(u -> u.displayName.toUpperCase().contains(upperPattern));
    }

    /**
     * Find users according to their number of coins found.
     *
     * @param minCoins the minimum number of coins found by a user for it to match.
     * @param maxCoins the maximum number of coins above which a user won't be considered a match. -1 to ignore.
     * @return the stream of matching users.
     */
    public Observable<UserStat> findByCoins(long minCoins, long maxCoins) {
        return userService.findAll()
                .flatMap(u -> coinService.totalCoinsMinedBy(u)
                    .filter(coins -> coins >= minCoins && (maxCoins < 0 || coins <= maxCoins))
                    .map(coins -> new UserStat(u, -1d, coins))
                );
    }
}
