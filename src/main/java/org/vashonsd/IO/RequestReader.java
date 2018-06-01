package org.vashonsd.IO;

import java.util.concurrent.BlockingQueue;

/**
 * The RequestReader takes messages from the pub/sub service and puts them on the queue of requests to be handled.
 */
public interface RequestReader extends Runnable {

    public void produceTo(BlockingQueue<Request> q);

}
