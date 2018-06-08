package org.vashonsd.IO.Service;

import org.vashonsd.IO.Message;

public interface Writer {

    /**
     * Given a topic and a Response, reformat the Response in the format of the external service and push it to that topic.
     * @param topic Usually the username. The service needs to be able to create the topic and subscription to go with it.
     * @param msg The Response, to be reformatted to the native format of the messaging service.
     */
    void write(String topic, Message msg);
}
