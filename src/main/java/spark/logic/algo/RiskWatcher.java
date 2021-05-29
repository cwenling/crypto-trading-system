package spark.logic.algo;

import com.binance.api.client.domain.market.AggTrade;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.quartz.SchedulerException;
import spark.data.CachedOrderBook;
import spark.logic.message.EventBroker;
import spark.logic.message.EventListener;
import spark.logic.message.EventManager;
import spark.logic.schedule.ScheduledEvent;
import spark.logic.source.ScheduleManager;

public class RiskWatcher implements EventListener, Runnable {
    private EventManager eventManager;
    private ScheduleManager scheduleManager;
    private double factor;
    private int window;
    private DescriptiveStatistics volTradesAtAskPrice;
    private DescriptiveStatistics volTradesAtBidPrice;

    public RiskWatcher(EventManager eM, ScheduleManager sM, double factor, int window) {
        this.eventManager = eM;
        this.scheduleManager = sM;
        this.factor = factor;
        this.window = window;
        this.volTradesAtAskPrice = new DescriptiveStatistics(window);
        this.volTradesAtBidPrice = new DescriptiveStatistics(window);
    }

    public void initialize() {
        try {
            this.scheduleManager.periodicCallback(1000, "imbalance");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(CachedOrderBook orderBook) {
    }

    @Override
    public void handleEvent(ScheduledEvent scheduledEvent) {
        String tag = scheduledEvent.getTag();
        if (tag.equals("imbalance")) {
            if (volTradesAtBidPrice.getN() < window) {
                System.out.println("initializing imbalance");
            } else {
                double imbalance = volTradesAtAskPrice.getSum() / volTradesAtBidPrice.getSum();
                if (imbalance > factor) {
                    System.out.println("Buy!");
                } else if (1/imbalance > factor) {
                    System.out.println("Sell!");
                } else {
                    System.out.println("do nothing");
                }
            }
        }
    }

    @Override
    public void handleEvent(AggTrade aggTrade) {
        if (aggTrade.isBuyerMaker()) { // trade is done at bid price
            volTradesAtBidPrice.addValue(Double.parseDouble(aggTrade.getQuantity()));
            volTradesAtAskPrice.addValue(0);
        } else { // trade is done at ask price
            volTradesAtBidPrice.addValue(0);
            volTradesAtAskPrice.addValue(Double.parseDouble(aggTrade.getQuantity()));
        }
    }

    @Override
    public void run() {
        initialize();
        EventBroker aggTradeEventBroker = this.eventManager.getAggTradeEventBroker();
        EventBroker scheduledEventBroker = this.eventManager.getScheduledEventEventBroker();
        while (true) {
            try {
                handleEvent((AggTrade) aggTradeEventBroker.dequeue());
                handleEvent((ScheduledEvent) scheduledEventBroker.dequeue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
