package org.vashonsd.Dummy;

import org.junit.Before;
import org.junit.Test;
import org.vashonsd.IO.Message;

import static org.junit.Assert.*;

public class DummyReaderTest {

    private DummyReader dummyReader;

    @Before
    public void setUp() throws Exception {
        dummyReader = new DummyReader();
    }


    @Test
    public void testRead() throws InterruptedException {
        long startTime = System.currentTimeMillis();
        Message msg;
        while(System.currentTimeMillis() - startTime <=10000) {
            if ((msg = dummyReader.read()) != null) {
                System.out.println(msg);
            }
            Thread.sleep(1000);
        }
    }
}