package org.vashonsd.IO;

import org.vashonsd.IO.Service.Writer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Publisher implements Runnable {

    BlockingQueue<Message> messages;

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

    public void setMessages(BlockingQueue<Message> messages) {
        this.messages = messages;
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }
}
