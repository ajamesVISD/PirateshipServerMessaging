package org.vashonsd.Dummy;

import org.vashonsd.Message;
import org.vashonsd.IO.Publisher;
import org.vashonsd.IO.Subscriber;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * A simple game of games to
 */
public class DummyGame implements Runnable {
    Subscriber subscriber;
    Publisher publisher;

    final BlockingQueue<Message> messagesFromRequests = new LinkedBlockingDeque<>();
    final BlockingQueue<Message> messagesToResponses = new LinkedBlockingDeque<>();

    public DummyGame() {
        subscriber = new DummySubscriber();
        publisher = new DummyPublisher();
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(publisher);
        pool.execute(subscriber);
        Message msg;
        while(true) {
            if((msg = subscriber.pull()) != null) {
                System.out.println("The game sees the message");
                msg.setBody(msg.getBody() + ", new sent to response.");
                publisher.publish(msg);
            }
        }
    }
}
