package org.vashonsd.IO;

import org.vashonsd.IO.Service.Reader;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Subscriber implements Runnable {

    private volatile boolean running;
    private Reader reader;

    private BlockingQueue<Message> messages = new LinkedBlockingDeque<>();

    public Subscriber(Reader reader) {
        this.reader = reader;
    }

    public void stop() {
        running = false;
    }

    public void run() {
        running = true;
        Message msg;
        while(running) {
            if((msg = reader.read()) != null) {
//                System.out.println("The Subscriber is putting on the queue: " + msg.toString());
                messages.offer(msg);
            }
        }
    }

    public void setReader(Reader r) {
        reader = r;
    }

    public Message pull() {
        Message msg;
        if((msg = messages.poll()) != null) {
            return msg;
        } else {
            return null;
        }
    }

    public void setMessages(BlockingQueue<Message> messages) {
        this.messages = messages;
    }

}
