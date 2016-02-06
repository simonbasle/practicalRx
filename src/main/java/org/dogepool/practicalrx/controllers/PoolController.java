package org.dogepool.practicalrx.controllers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserStat;
import org.dogepool.practicalrx.services.PoolRateService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.services.StatService;
import org.dogepool.practicalrx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pool", produces = MediaType.APPLICATION_JSON_VALUE)
public class PoolController {

    @Autowired
    private UserService userService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private PoolService poolService;

    @Autowired
    private PoolRateService poolRateService;

    @Autowired
    private StatService statService;

    @RequestMapping("/ladder/hashrate")
    public List<UserStat> ladderByHashrate() {
        return rankingService.getLadderByHashrate();
    }

    @RequestMapping("/ladder/coins")
    public List<UserStat> ladderByCoins() {
        return rankingService.getLadderByCoins();
    }

    @RequestMapping("/hashrate")
    public Map<String, Object> globalHashRate() {
        Map<String, Object> json = new HashMap<>(2);
        double ghashrate = poolRateService.poolGigaHashrate();
        if (ghashrate < 1) {
            json.put("unit", "MHash/s");
            json.put("hashrate", ghashrate * 100d);
        } else {
            json.put("unit", "GHash/s");
            json.put("hashrate", ghashrate);
        }
        return json;
    }

    @RequestMapping("/miners")
    public Map<String, Object> miners() {
        int allUsers = userService.findAll().size();
        int miningUsers = poolService.miningUsers().size();
        Map<String, Object> json = new HashMap<>(2);
        json.put("totalUsers", allUsers);
        json.put("totalMiningUsers", miningUsers);
        return json;
    }

    @RequestMapping("/miners/active")
    public List<User> activeMiners() {
        return poolService.miningUsers();
    }

    @RequestMapping("/lastblock")
    public Map<String, Object> lastBlock() {
        LocalDateTime found = statService.lastBlockFoundDate();
        Duration foundAgo = Duration.between(found, LocalDateTime.now());

        User foundBy;
        try {
            foundBy = statService.lastBlockFoundBy();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("WARNING: StatService failed to return the last user to find a coin");
            foundBy = new User(-1, "BAD USER", "Bad User from StatService, please ignore", "", null);
        }
        Map<String, Object> json = new HashMap<>(2);
        json.put("foundOn", found.format(DateTimeFormatter.ISO_DATE_TIME));
        json.put("foundAgo", foundAgo.toMinutes());
        json.put("foundBy", foundBy);
        return json;
    }
}
