package org.vashonsd.IO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;

/**
 * Created by andy on 6/3/18.
 */
public class GoogleSubscriberTest {

    GoogleSubscriber googleSubscriber;
    BlockingQueue<Message> messages = new LinkedBlockingDeque<>();

    @Before
    public void setUp() throws Exception {
        googleSubscriber = new GoogleSubscriber();
    }

    @After
    public void tearDown() throws Exception {
        googleSubscriber.stop();
    }

    @Test
    public void testMessageCreation() throws InterruptedException {
        Message msg;
        msg = messages.take();
        System.out.println(msg);
    }
}