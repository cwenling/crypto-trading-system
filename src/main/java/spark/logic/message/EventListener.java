package spark.logic.message;

import com.binance.api.client.domain.market.OrderBook;

public interface EventListener extends Runnable {

    void handleEvent(OrderBook orderBook);

    // void handleEvent(ScheduleEvent timer);

}
