package spark.ui;


import com.binance.api.client.exception.BinanceApiException;
import spark.logic.source.BinanceConnector;
import spark.logic.source.MarketDataManager;

public class UiManager {

    private final UiUtil ui;

    public UiManager() {
        this.ui = new UiUtil();
    }

    public void run() {
        ui.print("Enter currency symbol corresponding to data to be retrieved: ");

        try {
            String symbol = ui.readInput();
            MarketDataManager mdm = new MarketDataManager();
            mdm.subscribeOrderBook(symbol);
            //mdm.printOrderBook(symbol);
        } catch (BinanceApiException e) {
            ui.print(e.getMessage());
            ui.close();
        }
    }

}
