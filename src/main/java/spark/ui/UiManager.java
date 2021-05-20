package spark.ui;


import com.binance.api.client.exception.BinanceApiException;
import spark.logic.algo.AnalyticsManager;
import spark.logic.message.EventManager;
import spark.logic.source.BinanceConnector;
import spark.logic.source.MarketDataManager;

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
            AnalyticsManager aM = new AnalyticsManager(eM);
            //mdm.subscribeOrderBook(symbol, eM);
            //mdm.printOrderBook(symbol);

            ScheduledExecutorService eS = Executors.newScheduledThreadPool(2);
            eS.execute(mdm);
            eS.execute(aM);

        } catch (BinanceApiException e) {
            ui.print(e.getMessage());
            ui.close();
        }
    }

}
