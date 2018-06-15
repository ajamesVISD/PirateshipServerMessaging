package org.vashonsd.IO.Service;

import org.vashonsd.Message;

public interface Reader {

    /**
     * The Reader brokers with the external messaging service and provides a Message or null when read() is called.
     * @return
     */
    Message read();

    /**
     * Use this to cause threads to stop gracefully.
     */
    void stop();
}
