package spark.logic.message;

import com.binance.api.client.domain.market.OrderBook;
import spark.data.CachedOrderBook;

public interface EventListener {

    void handleEvent(CachedOrderBook orderBook);

    // void handleEvent(ScheduleEvent timer);

}
