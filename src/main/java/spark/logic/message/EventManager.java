package spark.logic.message;

import com.binance.api.client.domain.market.OrderBook;
import spark.data.CachedOrderBook;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EventManager {

    private Set<EventListener> subscribers;
    private EventBroker<CachedOrderBook> broker;

    public EventManager() {
        this.subscribers = new TreeSet<>();
        this.broker = new EventBroker();
    }

    public void publish(CachedOrderBook orderBook) throws InterruptedException {
        //this.subscribers.forEach(s -> s.handleEvent(orderbook));
        this.broker.enqueue(orderBook);
    }

    public void addListener(EventListener listener) {
        this.subscribers.add(listener);
    }

    public EventBroker getEventBroker() {
        return this.broker;
    }

}
