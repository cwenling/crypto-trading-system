package spark.logic.algo;

import spark.data.CachedOrderBook;
import spark.logic.message.EventBroker;
import spark.logic.message.EventListener;
import spark.logic.message.EventManager;
import spark.logic.schedule.ScheduledEvent;

public class AnalyticsManager implements EventListener, Runnable {

    private EventManager eM;

    public AnalyticsManager(EventManager eM) {
        this.eM = eM;
    }

    @Override
    public void handleEvent(CachedOrderBook orderBook) {
        orderBook.printDepthCache();
    }

    @Override
    public void handleEvent(ScheduledEvent scheduledEvent) {

    }

    @Override
    public void run() {
        EventBroker eB = this.eM.getOrderBookEventBroker();
        while (true) {
            try {
                //System.out.println("printing cached orderbook");
                handleEvent((CachedOrderBook) eB.dequeue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
