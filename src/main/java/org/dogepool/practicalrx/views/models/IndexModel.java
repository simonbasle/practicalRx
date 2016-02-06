package org.dogepool.practicalrx.views.models;

import java.util.List;

import org.dogepool.practicalrx.domain.UserStat;

public class IndexModel {

    private List<UserStat> hashLadder;
    private List<UserStat> coinsLadder;
    private String poolName;
    private int miningUserCount;
    private Double gigaHashrate;
    private String dogeToUsdMessage;
    private String dogeToEurMessage;

    public List<UserStat> getHashLadder() {
        return hashLadder;
    }

    public void setHashLadder(List<UserStat> hashLadder) {
        this.hashLadder = hashLadder;
    }

    public List<UserStat> getCoinsLadder() {
        return coinsLadder;
    }

    public void setCoinsLadder(List<UserStat> coinsLadder) {
        this.coinsLadder = coinsLadder;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMiningUserCount() {
        return miningUserCount;
    }

    public void setMiningUserCount(int miningUserCount) {
        this.miningUserCount = miningUserCount;
    }

    public Double getGigaHashrate() {
        return gigaHashrate;
    }

    public void setGigaHashrate(Double gigaHashrate) {
        this.gigaHashrate = gigaHashrate;
    }

    public String getDogeToUsdMessage() {
        return dogeToUsdMessage;
    }

    public void setDogeToUsdMessage(String dogeToUsdMessage) {
        this.dogeToUsdMessage = dogeToUsdMessage;
    }

    public String getDogeToEurMessage() {
        return dogeToEurMessage;
    }

    public void setDogeToEurMessage(String dogeToEurMessage) {
        this.dogeToEurMessage = dogeToEurMessage;
    }
}
