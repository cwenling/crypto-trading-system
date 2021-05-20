package spark.logic.message;

import spark.data.CachedOrderBook;

import java.util.concurrent.ArrayBlockingQueue;

public class EventBroker<T> {

    private ArrayBlockingQueue<T> bQueue;

    public EventBroker() {
        this.bQueue = new ArrayBlockingQueue<T>(1000);
    }

    public void enqueue(T oBook) {
        boolean result = this.bQueue.offer(oBook);
        //System.out.println(result);
        //System.out.println(bQueue);

    }

    public T dequeue() throws InterruptedException {
        return this.bQueue.take();
    }

}
