package spark.impl;

import com.binance.api.client.BinanceApiCallback;
import com.binance.api.client.domain.event.DepthEvent;
import com.binance.api.client.impl.BinanceApiWebSocketClientImpl;
import com.binance.api.client.impl.BinanceApiWebSocketListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

import java.io.Closeable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class BinanceApiFastWebSocketImpl extends BinanceApiWebSocketClientImpl {

    private final OkHttpClient client;

    public BinanceApiFastWebSocketImpl(OkHttpClient client) {
        super(client);
        this.client = client;
    }

    public Closeable onDepthEvent(String symbols, BinanceApiCallback<DepthEvent> callback) {
        String channel = (String) Arrays.stream(symbols.split(",")).map(String::trim).map((s) -> {
            return String.format("%s@depth100ms", s);
        }).collect(Collectors.joining("/"));
        return this.createNewWebSocket(channel, new BinanceApiWebSocketListener(callback, DepthEvent.class));
    }

    public Closeable createNewWebSocket(String channel, BinanceApiWebSocketListener<?> listener) {
        String streamingUrl = String.format("%s/%s", "wss://stream.binance.com:9443/ws", channel);
        Request request = (new Request.Builder()).url(streamingUrl).build();
        WebSocket webSocket = this.client.newWebSocket(request, listener);
        return () -> {
            //int code = true;
            listener.onClosing(webSocket, 1000, (String)null);
            webSocket.close(1000, (String)null);
            listener.onClosed(webSocket, 1000, (String)null);
        };
    }
}
