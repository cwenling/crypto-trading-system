package spark.logic;

import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.domain.event.AggTradeEvent;
import com.binance.api.client.domain.event.DepthEvent;


public class RetrieveData {

    public static void main (String[] args) {
        BinanceApiWebSocketClient client = BinanceApiClientFactory.newInstance().newWebSocketClient();

        //printAggTradeEvents(client, "ethbtc");

        printOrderBookChanges(client, "btcbusd");
    }

    public static void printAggTradeEvents(BinanceApiWebSocketClient client, String symbols) {
        client.onAggTradeEvent(symbols , (AggTradeEvent response) -> {
            System.out.print("Price: ");
            System.out.print(response.getPrice());
            System.out.print(" | Quantity: ");
            System.out.println(response.getQuantity());
        });
    }

    public static void printOrderBookChanges(BinanceApiWebSocketClient client, String symbols) {
        client.onDepthEvent(symbols, (DepthEvent response) -> {
            System.out.println(response.getAsks());
        });
    }

}
