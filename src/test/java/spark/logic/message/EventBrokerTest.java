package spark.logic.message;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import spark.data.CachedOrderBook;

import java.util.concurrent.ArrayBlockingQueue;

public class EventBrokerTest {

    @Test
    public void enqueueEvent_validEvent_success() throws InterruptedException {
        EventBroker<Integer> broker = new EventBroker<>();
        broker.enqueue(4);
        broker.enqueue(99);
        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        queue.put(4);
        queue.put(99);
        Assertions.assertEquals(broker.getQueue().toArray()[0], queue.toArray()[0]);
        Assertions.assertEquals(broker.getQueue().toArray()[1], queue.toArray()[1]);
        Assertions.assertEquals(broker.getQueue().toArray()[1], 99);
    }

    @Test
    public void dequeueEvent_validEvent_success() throws InterruptedException {
        EventBroker<Integer> broker = new EventBroker<>();
        broker.enqueue(1);
        broker.enqueue(2);
        Assertions.assertEquals(1, broker.dequeue());
    }

}
