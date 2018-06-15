package org.vashonsd.IO;

import org.vashonsd.IO.Service.Writer;
import org.vashonsd.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The Publisher pushes Messages to the external service (e.g., Google PubSub).
 * The Writer is the component which writes to the external service, and should encapsulate all the details of
 * a) translating a Message into a format the service likes, and;
 * b) connecting to the service and sending the Message.
 */
public class Publisher implements Runnable {

    BlockingQueue<Message> messages = new LinkedBlockingDeque<>();

    Writer writer;
    volatile boolean running;

    public Publisher(Writer writer) {
        this.writer = writer;
        messages = new LinkedBlockingDeque<>();
    }

    public void stop() {
        running = false;
    }

    public void publish(Message m) {
        messages.offer(m);
    }

    public void run() {
        running = true;
        Message msg;
        while(running) {
            if((msg = messages.poll()) != null)
            writer.write(msg.getUuid(), msg);
        }
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}
