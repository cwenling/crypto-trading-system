package spark.logic.message;

import com.binance.api.client.domain.market.OrderBook;
import spark.data.CachedOrderBook;
import spark.logic.schedule.ScheduledEvent;

public interface EventListener {

    void handleEvent(CachedOrderBook orderBook);

    void handleEvent(ScheduledEvent scheduledEvent);

}
