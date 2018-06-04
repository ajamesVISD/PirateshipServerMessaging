package org.vashonsd.IO.Dummy;

import org.vashonsd.IO.Message;
import org.vashonsd.IO.Service.Reader;

import java.util.*;

public class DummyReader implements Reader {

    List<Integer> intlist = new ArrayList<Integer>();

    public DummyReader() {
        intlist.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10));
    }

    @Override
    public Message read() {
        if(!intlist.isEmpty()) {
            System.out.println("Making a new message");
            return new Message("james.elliott", intlist.remove(0).toString());
        } else {
            return null;
        }
    }
}
