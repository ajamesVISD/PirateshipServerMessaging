package org.vashonsd.IO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GooglePubSubWriterBuilderTest {

    GooglePubSubWriterBuilder builder;
    GooglePubSubWriterService service;
    String uuid;

    @Before
    public void setUp() {
        builder = new GooglePubSubWriterBuilder()
                .withProject("pirateship-data")
                .withRole("pirateship-data-editor");
        service = builder.build();
        uuid = "whatis";
    }

    @Test
    public void testPubSubWriterBuilder() {
        System.out.println(builder);
    }

    @Test
    public void testBuiltServiceWrites() {
        Response resp = new Response(uuid, "testing");
        service.pushResponse(uuid, resp);
    }
}