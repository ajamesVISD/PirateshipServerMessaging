package org.vashonsd.Dummy;

import org.vashonsd.IO.Message;
import org.vashonsd.IO.Service.Reader;

import java.time.Instant;
import java.time.temporal.TemporalField;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadLocalRandom;

import static java.time.temporal.ChronoField.INSTANT_SECONDS;

public class DummyReader implements Reader {

    BlockingQueue<String> messages;
    private volatile boolean running;

    class randomEmitter implements Runnable {

        @Override
        public void run() {
            while(running) {
                messages.offer("Message at " + Instant.now().toString());
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(4000) + 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public DummyReader() {
        running = true;
        messages = new LinkedBlockingDeque<>();
        Thread thread = new Thread(new randomEmitter());
        thread.start();
    }


    @Override
    public Message read() {
        String msg;
        if((msg = messages.poll()) != null) {
            return new Message("james.elliott", msg);
        } else {
            return null;
        }
    }

    @Override
    public void stop() {
        running = false;
    }
}
