package spark.logic.message;

import com.binance.api.client.domain.market.AggTrade;
import com.binance.api.client.domain.market.OrderBook;
import spark.data.CachedOrderBook;
import spark.logic.schedule.ScheduledEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class EventManager {

    private Set<EventListener> subscribers;
    private EventBroker<CachedOrderBook> orderBookEventBroker;
    private EventBroker<ScheduledEvent> scheduledEventEventBroker;
    private EventBroker<AggTrade> aggTradeEventBroker;

    public EventManager() {
        this.subscribers = new TreeSet<>();
        this.orderBookEventBroker = new EventBroker<CachedOrderBook>();
        this.scheduledEventEventBroker = new EventBroker<ScheduledEvent>();
        this.aggTradeEventBroker = new EventBroker<AggTrade>();
    }

    public void publish(CachedOrderBook orderBook) throws InterruptedException {
        //this.subscribers.forEach(s -> s.handleEvent(orderbook));
        this.orderBookEventBroker.enqueue(orderBook);
    }

    public void publish(ScheduledEvent scheduledEvent) throws InterruptedException {
        this.scheduledEventEventBroker.enqueue(scheduledEvent);
    }

    public void publish(AggTrade aggTrade) throws InterruptedException {
        this.aggTradeEventBroker.enqueue(aggTrade);
    }

    public void addListener(EventListener listener) {
        this.subscribers.add(listener);
    }

    public EventBroker getOrderBookEventBroker() {
        return this.orderBookEventBroker;
    }

    public EventBroker getScheduledEventEventBroker() {
        return this.scheduledEventEventBroker;
    }

    public EventBroker<AggTrade> getAggTradeEventBroker() {
        return aggTradeEventBroker;
    }
}
