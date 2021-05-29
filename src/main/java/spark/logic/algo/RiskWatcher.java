package spark.logic.algo;

import spark.data.CachedOrderBook;
import spark.logic.message.EventListener;
import spark.logic.schedule.ScheduledEvent;

public class RiskWatcher implements EventListener, Runnable {
    private double imbalance;

    public RiskWatcher() {
        this.imbalance = 0;
    }

    @Override
    public void handleEvent(CachedOrderBook orderBook) {

    }

    @Override
    public void handleEvent(ScheduledEvent scheduledEvent) {

    }

    @Override
    public void run() {

    }
}
