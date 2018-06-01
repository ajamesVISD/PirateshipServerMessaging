package org.vashonsd.IO;

import java.util.concurrent.BlockingQueue;

public abstract class AbstractResponseWriter implements ResponseWriter {

    BlockingQueue<Response> outboundResponses;
    MessageWriterService messageWriterService;
    boolean running;

    public AbstractResponseWriter(MessageWriterService messageWriterService) {
        this.messageWriterService = messageWriterService;
    }

    @Override
    public void consumeFrom(BlockingQueue<Response> q) {
        outboundResponses = q;
    }

    @Override
    public void run() {
        running = true;
        Response resp;
        while(running) {
            try {
                resp=outboundResponses.take();
                System.out.println("Writing to outbound: " + resp.getBody());
                messageWriterService.pushResponse(resp.getUuid(), resp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
