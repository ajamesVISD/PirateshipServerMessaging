package org.vashonsd.IO;

import org.vashonsd.IO.Service.Reader;

import java.util.concurrent.BlockingQueue;

public class Subscriber implements Runnable {

    private volatile boolean running;
    private Reader reader;

    private BlockingQueue<Message> messages;

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
            if((msg = reader.read()) != null)
                try {
                    messages.put(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }

    public void setReader(Reader r) {
        reader = r;
    }

    public void setMessages(BlockingQueue<Message> messages) {
        this.messages = messages;
    }

}
