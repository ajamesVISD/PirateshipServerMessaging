package org.vashonsd.IO;

import java.util.concurrent.BlockingQueue;

public interface ResponseWriter extends Runnable {

    public void consumeFrom(BlockingQueue<Response> q);
}
