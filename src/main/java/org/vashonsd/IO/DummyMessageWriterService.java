package org.vashonsd.IO;

public class DummyMessageWriterService implements MessageWriterService {
    @Override
    public void pushResponse(String topic, Response resp) {
        System.out.println(resp.toString());
    }
}
