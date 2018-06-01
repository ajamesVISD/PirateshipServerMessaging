package org.vashonsd.IO;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.*;

import java.io.IOException;

public class GooglePubSubWriterBuilder {

    private String role;
    private String project;
    private String topic;
    private TopicAdminClient topicAdminClient;
    private SubscriptionAdminClient subscriptionAdminClient;
    private Publisher publisher;
    private String subscription;
    private CredentialsProvider credentialsProvider;

    public GooglePubSubWriterBuilder() {
    }

    public GooglePubSubWriterBuilder withRole(String str) {
        role = str;
        GoogleCredentials credentials = new GoogleCredentialsBuilder()
                .withRole(role)
                .build();
        credentialsProvider = FixedCredentialsProvider.create(credentials);
        TopicAdminSettings topicAdminSettings;
        try {
            topicAdminSettings = TopicAdminSettings
                    .newBuilder()
                    .setCredentialsProvider(credentialsProvider)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            topicAdminSettings = null;
        }
        try {
            topicAdminClient = TopicAdminClient.create(topicAdminSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SubscriptionAdminSettings subscriptionAdminSettings;
        try {
            subscriptionAdminSettings = SubscriptionAdminSettings
                    .newBuilder()
                    .setCredentialsProvider(credentialsProvider)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            subscriptionAdminSettings = null;
        }
        try {
            subscriptionAdminClient = SubscriptionAdminClient.create(subscriptionAdminSettings);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public GooglePubSubWriterBuilder withProject(String str) {
        project = str;
        return this;
    }

    public GooglePubSubWriterBuilder withTopic(String str) {
        topic = str;
        return this;
    }

    public GooglePubSubWriterBuilder withSubscription(String str) {
        subscription = str;
        return this;
    }

    public GooglePubSubWriterService build() {
        return new GooglePubSubWriterService(
                project,
                topicAdminClient,
                subscriptionAdminClient,
                credentialsProvider);
    }

    @Override
    public String toString() {
        return this.topicAdminClient.toString() + "\n" + this.subscriptionAdminClient;
    }
}
