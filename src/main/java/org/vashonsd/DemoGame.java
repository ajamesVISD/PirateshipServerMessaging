package org.vashonsd;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class DemoGame implements Game {

    private final BlockingQueue<Message> messages = new LinkedBlockingDeque<>();

    @Override
    public void handle(Message m) {
        messages.offer(new Message(m.getUuid(), m.getBody() + " has been handled by the game."));
    }

    @Override
    public Message pull() {
        Message msg;
        if((msg = messages.poll()) != null) {
            return msg;
        } else {
            return null;
        }
    }
}
