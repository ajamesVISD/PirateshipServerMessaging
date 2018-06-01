package org.vashonsd.IO;

public class DummyResponseWriter extends AbstractResponseWriter {

    public DummyResponseWriter() {
        super(new DummyMessageWriterService());
    }

}
