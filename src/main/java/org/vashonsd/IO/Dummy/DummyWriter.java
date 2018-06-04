package org.vashonsd.IO.Dummy;

import org.vashonsd.IO.Message;
import org.vashonsd.IO.Service.Writer;

public class DummyWriter implements Writer {


    @Override
    public void write(String topic, Message msg) {
        System.out.println(msg.toString());
    }
}
