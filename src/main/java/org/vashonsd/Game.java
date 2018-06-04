package org.vashonsd;

import org.vashonsd.IO.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Game implements Runnable {

    private Publisher publisher;
    private Subscriber subscriber;

    final BlockingQueue<Message> messagesFromRequests = new LinkedBlockingDeque<>();
    final BlockingQueue<Message> messagesToResponses = new LinkedBlockingDeque<>();

    volatile boolean running;

    public Game() {
        publisher = new GooglePublisher();
        publisher.setMessages(messagesToResponses);
        subscriber = new GoogleSubscriber();
        subscriber.setMessages(messagesFromRequests);
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(publisher);
        pool.execute(subscriber);
        Message msg;
        while(running) {
            if((msg = messagesFromRequests.poll()) != null) {
                System.out.println("The game found an inbound message: " + msg);
                msg.setBody(msg.getBody() + "!! Get it!!");
                messagesToResponses.offer(msg);
            }
        }
    }
}
