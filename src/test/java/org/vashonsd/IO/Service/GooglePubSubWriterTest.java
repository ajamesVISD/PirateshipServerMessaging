package org.vashonsd.IO.Service;

import com.google.cloud.pubsub.v1.Publisher;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.vashonsd.Message;

import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

/**
 * Created by andy on 6/3/18.
 */
public class GooglePubSubWriterTest {

    GooglePubSubWriter googlePubSubWriter;

    @Before
    public void setUp() throws Exception {
        googlePubSubWriter = GooglePubSubWriter.fromBuilder().build();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testWrite() throws Exception {
        for (int i = 0; i < 10; i++) {
            googlePubSubWriter.write("james.elliott", new Message("james.elliott", "Once again, message #" + i));
        }
    }

    @Test
    public void testWriteToNewSubscription() throws Exception {
        String randomName = "student." + new Random().nextInt(100);
        googlePubSubWriter.write(randomName, new Message(randomName, "A message"));
    }

    @Test
    public void testNewTopic() {
        String uuid = UUID.randomUUID().toString();
        Publisher pub = googlePubSubWriter.setUpTopicAndSubscription(uuid);
        assertNotNull(pub);
    }
}