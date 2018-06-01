package org.vashonsd.IO;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.pubsub.v1.PubsubMessage;

public class GoogleRequestReader extends AbstractRequestReader {
    public GoogleRequestReader() {
        super(new GooglePubSubReaderBuilder()
                    .withRole("pirateship-requests-consumer")
                    .withProjectId("pirateship-requests")
                    .withSubscription("requests")
                    .build()
        );
    }
}
