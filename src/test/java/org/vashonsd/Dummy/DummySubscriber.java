package org.vashonsd.Dummy;

import org.vashonsd.IO.Subscriber;

public class DummySubscriber extends Subscriber {

    public DummySubscriber() {
        super(new DummyReader());
    }
}
