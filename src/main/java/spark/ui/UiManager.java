package spark.ui;


import com.binance.api.client.exception.BinanceApiException;
import org.quartz.SchedulerException;
import spark.logic.algo.AnalyticsManager;
import spark.logic.algo.CrossoverManager;
import spark.logic.algo.RiskWatcher;
import spark.logic.message.EventManager;
import spark.logic.source.BinanceConnector;
import spark.logic.source.MarketDataManager;
import spark.logic.source.ScheduleManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class UiManager {

    private final UiUtil ui;

    public UiManager() {
        this.ui = new UiUtil();
    }

    public void run() {
        ui.print("Enter currency symbol corresponding to data to be retrieved: ");

        try {
            String symbol = ui.readInput();
            EventManager eM = new EventManager();
            MarketDataManager mdm = new MarketDataManager(eM, symbol);
            ScheduleManager sM = new ScheduleManager(eM);
            //AnalyticsManager aM = new AnalyticsManager(eM);
            //mdm.subscribeOrderBook(symbol, eM);
            //mdm.printOrderBook(symbol);
            CrossoverManager cM = new CrossoverManager(eM, sM, 1000, 3000, 10, 20);
            RiskWatcher riskWatcher = new RiskWatcher(eM, sM, 1.5, 10);

            ScheduledExecutorService eS = Executors.newScheduledThreadPool(3);
            eS.execute(mdm);
            eS.execute(cM);
            eS.execute(riskWatcher);

        } catch (BinanceApiException | SchedulerException e) {
            ui.print(e.getMessage());
            ui.close();
        }
    }

}
