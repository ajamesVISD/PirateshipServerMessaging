package org.vashonsd.Dummy;

import org.vashonsd.Message;
import org.vashonsd.IO.Service.Writer;

public class DummyWriter implements Writer {


    @Override
    public void write(String topic, Message msg) {
        System.out.println(msg.toString());
    }
}
