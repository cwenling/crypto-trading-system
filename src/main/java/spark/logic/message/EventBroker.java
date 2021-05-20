package spark.logic.message;

import spark.data.CachedOrderBook;

import java.util.concurrent.ArrayBlockingQueue;

public class EventBroker<T> {

    private ArrayBlockingQueue<T> bQueue;

    public EventBroker() {
        this.bQueue = new ArrayBlockingQueue<T>(1000);
    }

    public void enqueue(T event) throws InterruptedException {
        this.bQueue.put(event);
    }

    public T dequeue() throws InterruptedException {
        return this.bQueue.take();
    }

    public ArrayBlockingQueue getQueue() {
        return bQueue;
    }

}
