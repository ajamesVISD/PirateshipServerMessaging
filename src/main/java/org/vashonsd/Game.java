package org.vashonsd;

import org.vashonsd.IO.Message;

public interface Game {
    Message handle(Message m);
}
