package org.dogepool.practicalrx.controllers;

import java.util.Map;

import org.dogepool.practicalrx.domain.UserStat;
import org.dogepool.practicalrx.services.ExchangeRateService;
import org.dogepool.practicalrx.services.PoolRateService;
import org.dogepool.practicalrx.services.PoolService;
import org.dogepool.practicalrx.services.RankingService;
import org.dogepool.practicalrx.views.models.IndexModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * A utility controller that displays the welcome message as HTML on root endpoint.
 */
@Controller
public class IndexController {

    @Autowired
    private RankingService rankService;

    @Autowired
    private PoolService poolService;

    @Autowired
    private PoolRateService poolRateService;

    @Autowired
    private ExchangeRateService exchangeRateService;

    @RequestMapping("/")
    public String index(Map<String, Object> model) {
        //prepare a model
        IndexModel idxModel = new IndexModel();
        idxModel.setHashLadder(rankService.getLadderByHashrate());
        idxModel.setCoinsLadder(rankService.getLadderByCoins());
        idxModel.setPoolName(poolService.poolName());
        idxModel.setMiningUserCount(poolService.miningUsers().size());
        idxModel.setGigaHashrate(poolRateService.poolGigaHashrate());
        try {
            Double dogeToDollar = exchangeRateService.dogeToCurrencyExchangeRate("USD");
            idxModel.setDogeToUsdMessage("1 DOGE = " + dogeToDollar + "$");
        } catch (Exception e) {
            idxModel.setDogeToUsdMessage("1 DOGE = ??$, couldn't get the exchange rate - " + e.getMessage());
        }
        try {
            Double dogeToEuro = exchangeRateService.dogeToCurrencyExchangeRate("EUR");
            idxModel.setDogeToEurMessage("1 DOGE = " + dogeToEuro + "€");
        } catch (Exception e) {
            idxModel.setDogeToEurMessage("1 DOGE = ??€, couldn't get the exchange rate - " + e.getMessage());
        }

        //populate the model and call the template
        model.put("model", idxModel);
        return "index";
    }
}
