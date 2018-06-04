package org.vashonsd.IO.Service;

import junit.framework.TestCase;
import org.vashonsd.IO.Message;

/**
 * Created by andy on 6/3/18.
 */
public class GooglePubSubReaderTest extends TestCase {

    GooglePubSubReader googlePubSubReader;

    public void setUp() throws Exception {
        super.setUp();
        googlePubSubReader = GooglePubSubReader.fromBuilder()
                .withRole("pirateship-requests-consumer")
                .withProjectId("pirateship-requests")
                .withSubscription("requests")
                .build();
    }

    public void tearDown() throws Exception {
        googlePubSubReader.stop();
        googlePubSubReader = null;
    }

    public void testRead() throws Exception {
        Message msg;
        while(true) {
            if((msg = googlePubSubReader.read()) != null) {
                System.out.println(msg.getBody());
            }
        }
    }
}