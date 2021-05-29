package spark.logic.source;

import spark.logic.message.EventManager;

import java.util.HashMap;
import java.util.Map;

public class MarketDataManager implements Runnable {

    private Map<String, BinanceConnector> connections;
    EventManager eM;
    String sym;

    public MarketDataManager (EventManager eM, String s) {
        this.connections = new HashMap<>();
        this.eM = eM;
        this.sym = s;
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

    public void subscribeOrderBook(String symbol, EventManager eM) throws InterruptedException {
        if (!this.isSubscribed(symbol)) {
            this.subscribeGateway(symbol);
        }
        this.getConnection(symbol).startDepthEventStreaming(symbol, eM);
    }

    public void subscribeTrades(String symbol, EventManager eM) throws InterruptedException {
        if (!this.isSubscribed(symbol)) {
            this.subscribeGateway(symbol);
        }
        this.getConnection(symbol).startAggTradesEventStreaming(symbol, eM);
    }

    @Override
    public void run() {
        try {
            subscribeOrderBook(sym, eM);
            subscribeTrades(sym, eM);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
