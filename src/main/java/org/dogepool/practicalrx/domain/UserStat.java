package org.dogepool.practicalrx.domain;

/**
 * A class that combines a {@link User User's} information with his/her stats,
 * like last known hashrate and total number of dogecoins mined since joining the pool.
 */
public class UserStat {

    public final User user;
    public final double hashrate;
    public final long totalCoinsMined;

    public UserStat(User user, double hashrate, long totalCoinsMined) {
        this.user = user;
        this.hashrate = hashrate;
        this.totalCoinsMined = totalCoinsMined;
    }
}
