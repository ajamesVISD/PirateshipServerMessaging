package org.vashonsd.IO;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractRequestReader implements RequestReader {

    private boolean running;
    private MessageReaderService messageReaderService;
    private BlockingQueue<Request> inboundRequests;

    public AbstractRequestReader(MessageReaderService messageReaderService) {
        this.messageReaderService = messageReaderService;
    }

    @Override
    public void produceTo(BlockingQueue<Request> q) {
        inboundRequests = q;
    }

    @Override
    public void run() {
        running = true;
        Request resp;
        while(running) {
            if((resp = messageReaderService.pullRequests()) != null)
            inboundRequests.offer(resp);
        }
    }

    public void setMessageReaderService(MessageReaderService m) {
        messageReaderService = m;
    }
}
