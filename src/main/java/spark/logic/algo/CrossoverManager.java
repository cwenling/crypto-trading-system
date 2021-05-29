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

public class CrossoverManager implements EventListener, Runnable {

    private final EventManager eventManager;
    private final ScheduleManager scheduleManager;
    private final int period1;
    private final int period2;
    private final DescriptiveStatistics sma1;
    private final DescriptiveStatistics sma2;
    private CachedOrderBook lastOrderBook = null;

    public CrossoverManager(EventManager eM, ScheduleManager sM, int period1, int period2, int windowSize1, int windowSize2) {
        this.eventManager = eM;
        this.scheduleManager = sM;
        this.period1 = period1;
        this.period2 = period2;
        this.sma1 = new DescriptiveStatistics(windowSize1);
        this.sma2 = new DescriptiveStatistics(windowSize2);
    }

    public void initialize() {
        try {
            this.scheduleManager.periodicCallback(period1, "sma1");
            this.scheduleManager.periodicCallback(period2, "sma2");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(CachedOrderBook orderBook) {
        lastOrderBook = orderBook;
    }

    @Override
    public void handleEvent(ScheduledEvent scheduledEvent) {
        computeSMA(scheduledEvent);
    }

    @Override
    public void handleEvent(AggTrade aggTrade) {
    }

    private void computeSMA(ScheduledEvent scheduledEvent) {
        String tag = scheduledEvent.getTag();
        if (lastOrderBook == null) {
            return;
        } else if (tag.equals("sma1")) {
            sma1.addValue(computeMid(lastOrderBook));
            System.out.println(tag + " - " + sma1.getMean());
        } else if (tag.equals("sma2")) {
            sma2.addValue(computeMid(lastOrderBook));
            System.out.println(tag + " - " + sma2.getMean());
        }


    }

    private double computeMid(CachedOrderBook orderBook) {
        double bestBid = orderBook.getBestBid().getValue().doubleValue();
        double bestAsk = orderBook.getBestAsk().getValue().doubleValue();
        return 0.5 * (bestAsk + bestBid);
    }

    @Override
    public void run() {
        initialize();
        EventBroker orderBookEventBroker = this.eventManager.getOrderBookEventBroker();
        EventBroker scheduledEventBroker = this.eventManager.getScheduledEventEventBroker();
        while (true) {
            try {
                handleEvent((CachedOrderBook) orderBookEventBroker.dequeue());
                handleEvent((ScheduledEvent) scheduledEventBroker.dequeue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
