package org.dogepool.practicalrx.controllers;

import java.util.Map;

import org.dogepool.practicalrx.domain.User;
import org.dogepool.practicalrx.domain.UserProfile;
import org.dogepool.practicalrx.error.*;
import org.dogepool.practicalrx.error.Error;
import org.dogepool.practicalrx.services.*;
import org.dogepool.practicalrx.views.models.MinerModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.async.DeferredResult;

@Controller(value = "/miner")
public class UserProfileController {
    
    @Value(value = "${avatar.api.baseUrl}")
    private String avatarBaseUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private RankingService rankingService;

    @Autowired
    private HashrateService hashrateService;

    @Autowired
    private CoinService coinService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/miner/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DeferredResult<UserProfile> profile(@PathVariable int id) {
        DeferredResult<UserProfile> deferred = new DeferredResult<>(90000);
        User user = userService.getUser(id);
        if (user == null) {
            deferred.setErrorResult(new DogePoolException("Unknown miner", Error.UNKNOWN_USER, HttpStatus.NOT_FOUND));
            return deferred;
        } else {
            //find the avatar's url
            ResponseEntity<Map> avatarResponse = restTemplate.getForEntity(avatarBaseUrl + "/" + user.avatarId, Map.class);
            if (avatarResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, ?> avatarInfo = avatarResponse.getBody();
                String avatarUrl = (String) avatarInfo.get("large");
                String smallAvatarUrl = (String) avatarInfo.get("small");

                //complete with other information
                double hash = hashrateService.hashrateFor(user);
                long rankByHash = rankingService.rankByHashrate(user);
                long rankByCoins = rankingService.rankByCoins(user);
                long coins = coinService.totalCoinsMinedBy(user);

                deferred.setResult(new UserProfile(user, hash, coins, avatarUrl, smallAvatarUrl, rankByHash, rankByCoins));
                return deferred;
            } else {
                deferred.setErrorResult(new DogePoolException("Unable to get avatar info", Error.UNREACHABLE_SERVICE,
                        avatarResponse.getStatusCode()));
                return deferred;
            }
        }
    }

    @RequestMapping(value = "/miner/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public DeferredResult<String> miner(Map<String, Object> model, @PathVariable int id) {
        DeferredResult<String> stringResponse = new DeferredResult<>(90000);
        User user = userService.getUser(id);
        if (user == null) {
            stringResponse.setErrorResult(new DogePoolException("Unknown miner", Error.UNKNOWN_USER, HttpStatus.NOT_FOUND));
            return stringResponse;
        } else {
            //find the avatar's url
            ResponseEntity<Map> avatarResponse = restTemplate.getForEntity(avatarBaseUrl + "/" + user.avatarId, Map.class);
            if (avatarResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, ?> avatarInfo = avatarResponse.getBody();
                String avatarUrl = (String) avatarInfo.get("large");
                String smallAvatarUrl = (String) avatarInfo.get("small");

                //complete with other information
                double hash = hashrateService.hashrateFor(user);
                long rankByHash = rankingService.rankByHashrate(user);
                long rankByCoins = rankingService.rankByCoins(user);
                long coins = coinService.totalCoinsMinedBy(user);

                UserProfile profile = new UserProfile(user, hash, coins, avatarUrl, smallAvatarUrl, rankByHash, rankByCoins);
                MinerModel minerModel = new MinerModel();
                minerModel.setAvatarUrl(profile.avatarUrl);
                minerModel.setSmallAvatarUrl(profile.smallAvatarUrl);
                minerModel.setBio(user.bio);
                minerModel.setDisplayName(user.displayName);
                minerModel.setNickname(user.nickname);
                minerModel.setRankByCoins(profile.rankByCoins);
                minerModel.setRankByHash(profile.rankByHash);
                model.put("minerModel", minerModel);
                stringResponse.setResult("miner");

                return stringResponse;
            } else {
                stringResponse.setErrorResult(new DogePoolException("Unable to get avatar info", Error.UNREACHABLE_SERVICE,
                        avatarResponse.getStatusCode()));
                return stringResponse;
            }
        }
}
}
