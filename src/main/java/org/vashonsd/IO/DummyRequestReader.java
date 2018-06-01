package org.vashonsd.IO;

public class DummyRequestReader extends AbstractRequestReader {

    public DummyRequestReader() {
        super(new DummyMessageReaderService());
    }
}
