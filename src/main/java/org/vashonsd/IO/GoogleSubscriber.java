package org.vashonsd.IO;

import org.vashonsd.IO.Service.GooglePubSubReader;

public class GoogleSubscriber extends Subscriber {
    public GoogleSubscriber() {
        super(GooglePubSubReader.fromBuilder()
                        .build()
        );
    }
}
