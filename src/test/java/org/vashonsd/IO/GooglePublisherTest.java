package org.vashonsd.IO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vashonsd.IO.Service.GooglePubSubWriter;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

public class GooglePublisherTest {

    Publisher publisher;
    Properties properties;

    @Before
    public void setUp() throws Exception {
        properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("server.properties"));
        publisher = new Publisher(
                GooglePubSubWriter.fromBuilder()
                        .withProject(properties.getProperty("response-project"))
                        .withRole(properties.getProperty("response-role"))
                        .build()
        );
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPublisherPublishes() throws Exception {
        Message m = new Message("james.elliott", "A message sent from the unit test.");
        publisher.publish(m);
    }
}