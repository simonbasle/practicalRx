package org.dogepool.practicalrx.controllers;

import java.util.Map;

import org.dogepool.practicalrx.domain.UserProfile;
import org.dogepool.practicalrx.error.DogePoolException;
import org.dogepool.practicalrx.error.Error;
import org.dogepool.practicalrx.services.CoinService;
import org.dogepool.practicalrx.services.HashrateService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.services.UserService;
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
import rx.Observable;
import rx.schedulers.Schedulers;

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
        DeferredResult<UserProfile> deferred = new DeferredResult<>();

        userService.getUser(id)
                .single()
                .onErrorResumeNext(Observable.error(new DogePoolException("Unknown miner", Error.UNKNOWN_USER,
                        HttpStatus.NOT_FOUND)))
                //find the avatar's url
                .flatMap(user -> {
                    ResponseEntity<Map> avatarResponse =
                            restTemplate.getForEntity(avatarBaseUrl + "/" + user.avatarId, Map.class);
                    if (avatarResponse.getStatusCode().is2xxSuccessful()) {
                        Map<String, ?> avatarInfo = avatarResponse.getBody();
                        String avatarUrl = (String) avatarInfo.get("large");
                        String smallAvatarUrl = (String) avatarInfo.get("small");

                        //complete with other information
                        return Observable.zip(
                                hashrateService.hashrateFor(user),
                                coinService.totalCoinsMinedBy(user),
                                rankingService.rankByHashrate(user),
                                rankingService.rankByCoins(user),
                                //return the full profile
                                (hash, coins, rankByHash, rankByCoins) -> new UserProfile(user, hash, coins,
                                        avatarUrl, smallAvatarUrl, rankByHash, rankByCoins));
                    } else {
                        return Observable.error(new DogePoolException("Unable to get avatar info",
                                Error.UNREACHABLE_SERVICE, avatarResponse.getStatusCode()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(userProfile -> deferred.setResult(userProfile),
                        error -> deferred.setErrorResult(error));

        return deferred;
    }

    @RequestMapping(value = "/miner/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public DeferredResult<String> miner(Map<String, Object> model, @PathVariable int id) {
        DeferredResult<String> deferred = new DeferredResult<>();

        userService.getUser(id)
                   .single()
                .onErrorResumeNext(Observable.error(new DogePoolException("Unknown miner", Error.UNKNOWN_USER,
                        HttpStatus.NOT_FOUND)))
                        //find the avatar's url
                .flatMap(user -> {
                    ResponseEntity<Map> avatarResponse =
                            restTemplate.getForEntity(avatarBaseUrl + "/" + user.avatarId, Map.class);
                    if (avatarResponse.getStatusCode().is2xxSuccessful()) {
                        Map<String, ?> avatarInfo = avatarResponse.getBody();
                        String avatarUrl = (String) avatarInfo.get("large");
                        String smallAvatarUrl = (String) avatarInfo.get("small");

                        //complete with other information
                        return Observable.zip(
                                hashrateService.hashrateFor(user),
                                coinService.totalCoinsMinedBy(user),
                                rankingService.rankByHashrate(user),
                                rankingService.rankByCoins(user),
                                //create a model for the view
                                (hash, coins, rankByHash, rankByCoins) -> {
                                    MinerModel minerModel = new MinerModel();
                                    minerModel.setAvatarUrl(avatarUrl);
                                    minerModel.setSmallAvatarUrl(smallAvatarUrl);
                                    minerModel.setBio(user.bio);
                                    minerModel.setDisplayName(user.displayName);
                                    minerModel.setNickname(user.nickname);
                                    minerModel.setRankByCoins(rankByCoins);
                                    minerModel.setRankByHash(rankByHash);
                                    return minerModel;
                                });
                    } else {
                        return Observable.error(new DogePoolException("Unable to get avatar info",
                                Error.UNREACHABLE_SERVICE, avatarResponse.getStatusCode()));
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(
                        minerModel -> model.put("minerModel", minerModel),
                        error -> deferred.setErrorResult(error),
                        () -> deferred.setResult("miner")
                );

        return deferred;
    }
}
