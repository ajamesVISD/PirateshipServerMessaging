package org.vashonsd;

import org.vashonsd.IO.Message;

public class DemoGame implements Game {
    @Override
    public Message handle(Message m) {
        return new Message(m.getUuid(), m.getBody() + " has been handled by the game.");
    }
}
