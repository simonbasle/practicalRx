package org.dogepool.practicalrx.views.models;

public class MinerModel {

    private String smallAvatarUrl;
    private String displayName;
    private String nickname;
    private String bio;
    private String avatarUrl;
    private long rankByCoins;
    private long rankByHash;

    public String getSmallAvatarUrl() {
        return smallAvatarUrl;
    }

    public void setSmallAvatarUrl(String smallAvatarUrl) {
        this.smallAvatarUrl = smallAvatarUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public long getRankByCoins() {
        return rankByCoins;
    }

    public void setRankByCoins(long rankByCoins) {
        this.rankByCoins = rankByCoins;
    }

    public long getRankByHash() {
        return rankByHash;
    }

    public void setRankByHash(long rankByHash) {
        this.rankByHash = rankByHash;
    }
}
