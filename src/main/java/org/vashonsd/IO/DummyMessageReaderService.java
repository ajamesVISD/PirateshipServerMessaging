package org.vashonsd.IO;

import jdk.nashorn.internal.ir.Block;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class DummyMessageReaderService implements MessageReaderService {

    List<Integer> intlist = new ArrayList<Integer>();

    public DummyMessageReaderService() {
        intlist.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
    }

    @Override
    public Request pullRequests() {
        if(!intlist.isEmpty()) {
            System.out.println("Making a new message");
            return new Request("james.elliott", intlist.remove(0).toString());
        } else {
            return null;
        }
    }
}
