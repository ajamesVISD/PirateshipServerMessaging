package org.vashonsd.IO;

import com.google.auth.oauth2.ServiceAccountCredentials;
import org.junit.Assert;
import org.junit.Test;
import org.omg.PortableServer.Servant;

import static org.junit.Assert.*;

public class GoogleCredentialsBuilderTest {

    @Test
    public void testBuild() {
        ServiceAccountCredentials creds = new GoogleCredentialsBuilder()
                .withRole("pirateship-requests-consumer")
                .build();
        System.out.println(creds.toString());
        Assert.assertNotNull(creds);
    }
}