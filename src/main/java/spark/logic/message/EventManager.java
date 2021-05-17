package spark.logic.message;

import com.binance.api.client.domain.market.OrderBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EventManager {

    private Set<EventListener> subscribers;

    public EventManager() {
        this.subscribers = new TreeSet<>();
    }

    public void publish(OrderBook orderbook) {
        this.subscribers.forEach(s -> s.handleEvent(orderbook));
    }

    public void addListener(EventListener listener) {
        this.subscribers.add(listener);
    }

}
