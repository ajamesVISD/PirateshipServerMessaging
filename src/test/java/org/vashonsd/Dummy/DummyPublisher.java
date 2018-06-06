package org.vashonsd.Dummy;

import org.vashonsd.IO.Publisher;

public class DummyPublisher extends Publisher {

    public DummyPublisher() {
        super(new DummyWriter());
    }

}
