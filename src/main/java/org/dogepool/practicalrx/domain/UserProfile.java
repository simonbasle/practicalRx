package org.dogepool.practicalrx.domain;

public class UserProfile extends UserStat {

    public final String avatarUrl;

    public final String smallAvatarUrl;

    public final long rankByHash;

    public final long rankByCoins;

    public UserProfile(User user, double hashrate, long totalCoinsMined, String avatarUrl, String smallAvatarUrl,
            long rankByHash,
            long rankByCoins) {
        super(user, hashrate, totalCoinsMined);
        this.avatarUrl = avatarUrl;
        this.smallAvatarUrl = smallAvatarUrl;
        this.rankByHash = rankByHash;
        this.rankByCoins = rankByCoins;
    }
}
