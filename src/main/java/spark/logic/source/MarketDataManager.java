package spark.logic.source;

import java.util.HashMap;
import java.util.Map;

public class MarketDataManager implements Runnable {

    private Map<String, BinanceConnector> connections;

    public MarketDataManager () {
        this.connections = new HashMap<>();
    }

    private boolean isSubscribed(String symbol) {
        return this.connections.containsKey(symbol);
    }

    private void addConnectionGateway(BinanceConnector connection) {
        this.connections.put(connection.getSymbol(), connection);
    }

    private BinanceConnector getConnection(String symbol) {
        return this.connections.get(symbol);
    }

    public void subscribeGateway(String symbol) {
        if (this.isSubscribed(symbol)) {
            return;
        }
        BinanceConnector connection = new BinanceConnector(symbol);
        this.addConnectionGateway(connection);
    }

    public void subscribeOrderBook(String symbol) {
        if (!this.isSubscribed(symbol)) {
            this.subscribeGateway(symbol);
        }
        this.getConnection(symbol).startDepthEventStreaming(symbol);
    }

    public void subscribeTrades(String symbol) {
        if (!this.isSubscribed(symbol)) {
            this.subscribeGateway(symbol);
        }
        this.getConnection(symbol).startAggTradesEventStreaming(symbol);
    }

    public void printOrderBook(String symbol) {
        BinanceConnector connection = this.getConnection(symbol);
        connection.printDepthCache();
    }

    @Override
    public void run() {

    }
}
