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

/**
 * A Server packages a Publisher and a Subscriber together and runs them in a thread.
 *
 * Since the server is itself Runnable, it can be run in a thread, or just called via run().
 */
public class Server implements Runnable {

    private Publisher publisher;
    private Subscriber subscriber;
    private Properties properties;
    private Game game;

    final BlockingQueue<Message> messagesToResponses = new LinkedBlockingDeque<>();

    volatile boolean running;

    public Server() {

    }

    @Override
    public void run() {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.execute(publisher);
        pool.execute(subscriber);
        Message msg;
        while(true) {
            if((msg = subscriber.pull()) != null) {
                publisher.publish(game.handle(msg));
            }
        }
    }

    public static Builder fromBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private Publisher publisher;
        private Subscriber subscriber;
        private Properties properties;
        private Game game;

        private Builder() {
            InputStream propertiesFile = getClass().getClassLoader().getResourceAsStream("server.properties");
            try {
                properties = new Properties();
                properties.load(propertiesFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static Builder aServer() {
            return new Builder();
        }

        public Builder withGooglePubsub() {
            publisher = new Publisher(GooglePubSubWriter.fromBuilder()
                    .withProject(properties.getProperty("response-project"))
                    .withRole(properties.getProperty("response-role"))
                    .build());
            subscriber = new Subscriber(GooglePubSubReader.fromBuilder()
                    .withProjectId(properties.getProperty("requests-project"))
                    .withSubscription(properties.getProperty("requests-subscription"))
                    .withRole(properties.getProperty("requests-role"))
                    .build());
            return this;
        }

        public Builder withProperties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public Builder withGame(Game game) {
            this.game = game;
            return this;
        }

        public Server build() {
            Server server = new Server();
            server.properties = this.properties;
            server.game = this.game;
            server.publisher = this.publisher;
            server.subscriber = this.subscriber;
            return server;
        }
    }
}
