package org.vashonsd.IO;

public interface MessageWriterService {

    /**
     * Given a topic and a Response, reformat the Response in the format of the pubsub service and push it to that topic.
     * @param topic Usually the username. The service needs to be able to create the topic and subscription to go with it.
     * @param resp The Response, to be reformatted to the native format of the messaging service.
     */
    void pushResponse(String topic, Response resp);
}
