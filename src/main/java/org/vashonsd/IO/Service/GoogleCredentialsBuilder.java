package org.vashonsd.IO.Service;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.Subscriber;

import java.io.IOException;
import java.io.InputStream;

public class GoogleCredentialsBuilder {
    private String role;

    public GoogleCredentialsBuilder() {
    }

    public GoogleCredentialsBuilder withRole(String str) {
        role = str;
        return this;
    }

    public ServiceAccountCredentials build() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream serviceAccountStream = classLoader.getResourceAsStream(role + ".json");
        try {
            return ServiceAccountCredentials.fromStream(serviceAccountStream);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
