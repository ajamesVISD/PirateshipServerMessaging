package org.vashonsd;

import org.vashonsd.IO.*;
import org.vashonsd.IO.Service.GooglePubSubReader;
import org.vashonsd.IO.Service.GooglePubSubWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class Game implements Runnable {

    private Publisher publisher;
    private Subscriber subscriber;
    private Properties properties;

    final BlockingQueue<Message> messagesToResponses = new LinkedBlockingDeque<>();

    volatile boolean running;

    public Game() {
        InputStream propertiesFile = getClass().getClassLoader().getResourceAsStream("server.properties");
        try {
            properties = new Properties();
            properties.load(propertiesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        publisher = new Publisher(GooglePubSubWriter.fromBuilder()
            .withProject(properties.getProperty("response-project"))
            .withRole(properties.getProperty("response-role"))
            .build());
        subscriber = new Subscriber(GooglePubSubReader.fromBuilder()
            .withProjectId(properties.getProperty("requests-project"))
            .withSubscription(properties.getProperty("requests-subscription"))
            .withRole(properties.getProperty("requests-role"))
            .build());
    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(publisher);
        pool.execute(subscriber);
        Message msg;
        while(true) {
            if((msg = subscriber.pull()) != null) {
                msg.setBody(msg.getBody() + "!!! Get hype!!!");
                publisher.publish(msg);
            }
        }
    }
}
