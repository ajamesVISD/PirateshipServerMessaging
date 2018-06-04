package org.vashonsd.IO;

import org.vashonsd.IO.Service.GooglePubSubWriter;

public class GooglePublisher extends Publisher {
    public GooglePublisher() {
        super(GooglePubSubWriter.fromBuilder()
                        .build()
        );
    }
}
