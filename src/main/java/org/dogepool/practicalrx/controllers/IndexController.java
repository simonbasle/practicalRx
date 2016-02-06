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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;
import rx.Observable;

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
    public DeferredResult<ModelAndView> index(Map<String, Object> model) {
        DeferredResult<ModelAndView> deferredResult = new DeferredResult<>();

        //prepare the error catching observable for currency rates
        Observable<String> doge2usd = exchangeRateService.dogeToCurrencyExchangeRate("USD")
                .map(rate -> "1 DOGE = " + rate + "$")
                .onErrorReturn(e -> "1 DOGE = ??$, couldn't get the exchange rate - " + e);
        Observable<String> doge2eur = exchangeRateService.dogeToCurrencyExchangeRate("EUR")
                 .map(rate -> "1 DOGE = " + rate + "€")
                 .onErrorReturn(e -> "1 DOGE = ??€, couldn't get the exchange rate - " + e);
        //prepare a model
        Observable<IndexModel> modelZip = Observable.zip(
                rankService.getLadderByHashrate().toList(),
                rankService.getLadderByCoins().toList(),
                poolService.miningUsers().count(),
                poolRateService.poolGigaHashrate(),
                doge2usd,
                doge2eur,
                (lh, lc, muc, pgr, d2u, d2e) -> {
                    IndexModel idxModel = new IndexModel();
                    idxModel.setPoolName(poolService.poolName());
                    idxModel.setHashLadder(lh);
                    idxModel.setCoinsLadder(lc);
                    idxModel.setMiningUserCount(muc);
                    idxModel.setGigaHashrate(pgr);
                    idxModel.setDogeToUsdMessage(d2u);
                    idxModel.setDogeToEurMessage(d2e);
                    return idxModel;
                });

        //populate the model and call the template asynchronously
        modelZip.subscribe(
                idx -> deferredResult.setResult(new ModelAndView("index", "model", idx)),
                error -> deferredResult.setErrorResult(error));
        return deferredResult;
    }
}
