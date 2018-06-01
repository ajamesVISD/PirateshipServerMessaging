package org.vashonsd.IO;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.MessageReceiver;

public class GooglePubSubReaderBuilder {

    private String projectId;
    private String subscription;
    private String role;

    public GooglePubSubReaderBuilder() {
    }

    public GooglePubSubReaderBuilder withProjectId(String id) {
        projectId = id;
        return this;
    }

    public GooglePubSubReaderBuilder withSubscription(String str) {
        subscription = str;
        return this;
    }

    public GooglePubSubReaderBuilder withRole(String str) {
        role = str;
        return this;
    }

    public GooglePubSubReaderService build() {
        GoogleCredentials creds = new GoogleCredentialsBuilder()
                .withRole(role)
                .build();
        return new GooglePubSubReaderService(
                role,
                projectId,
                subscription,
                creds
        );
    }
}
