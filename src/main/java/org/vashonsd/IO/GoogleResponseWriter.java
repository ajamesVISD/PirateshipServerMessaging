package org.vashonsd.IO;

public class GoogleResponseWriter extends AbstractResponseWriter {
    public GoogleResponseWriter() {
        super( new GooglePubSubWriterBuilder()
                .withProject("pirateship-data")
                .withRole("pirateship-data-editor")
                .build()
        );
    }
}
