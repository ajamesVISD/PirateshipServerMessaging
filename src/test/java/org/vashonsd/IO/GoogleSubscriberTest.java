package org.vashonsd.IO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vashonsd.IO.Service.GooglePubSubReader;

import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by andy on 6/3/18.
 */
public class GoogleSubscriberTest {

    Subscriber subscriber;
    BlockingQueue<Message> messages = new LinkedBlockingDeque<>();
    ExecutorService pool;
    Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("server.properties"));
        subscriber = new Subscriber(
                GooglePubSubReader.fromBuilder()
                .withProjectId(properties.getProperty("requests-project"))
                .withSubscription(properties.getProperty("requests-subscription"))
                .withRole(properties.getProperty("requests-role"))
                .build()
        );
        pool = Executors.newCachedThreadPool();
        pool.submit(subscriber);
    }

    @After
    public void tearDown() throws Exception {
        subscriber.stop();
        pool.shutdown();
    }

    /**
     * This test requires someone to get onto the Google Console and push a message to the "requests" topic of pirateship-requests:
     *
     * https://console.cloud.google.com/cloudpubsub/topics/requests?project=pirateship-requests
     */
    @Test
    public void testSubscriberSubscribes() {
        Message msg;
        while(true) {
            if((msg = messages.poll()) != null) {
                System.out.println(msg);
            }
        }
    }
}