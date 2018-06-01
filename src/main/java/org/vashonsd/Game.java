package org.vashonsd;

import org.vashonsd.IO.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Game implements Runnable {

    final BlockingQueue<Response> responsesOut = new LinkedBlockingDeque<Response>();
    final BlockingQueue<Request> requestsIn = new LinkedBlockingDeque<Request>();

    RequestReader requestReader;
    ResponseWriter responseWriter;

    boolean running;

    public Game() {
        requestReader = new GoogleRequestReader();
        requestReader.produceTo(requestsIn);
        responseWriter = new GoogleResponseWriter();
        responseWriter.consumeFrom(responsesOut);
    }

    @Override
    public void run() {
        running = true;
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(requestReader);
        pool.execute(responseWriter);
        Request req;
        while(running) {
            if((req = requestsIn.poll()) != null) {
                Response resp = new Response(
                        req.getUuid(),
                        req.getBody() + "!! Get crazy!!"
                );
                responsesOut.offer(resp);
            }
        }
    }
}
