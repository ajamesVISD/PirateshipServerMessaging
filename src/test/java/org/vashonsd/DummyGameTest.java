package org.vashonsd;

import org.junit.Test;
import org.vashonsd.Dummy.DummyGame;

import static org.junit.Assert.*;

public class DummyGameTest {

    @Test
    public void testRun() {
        new DummyGame().run();
    }
}