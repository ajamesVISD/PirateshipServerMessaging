package org.vashonsd.IO.Service;

import com.google.api.core.ApiService.Listener;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import org.vashonsd.IO.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The ReaderService pulls from the correct Pub/Sub project + subscription and puts what it gets on the queue.
 */
public class GooglePubSubReader implements Reader {

    private final String projectId;

    private final String subscription;
    private final Subscriber subscriber;
    GoogleCredentials credentials;
    final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<PubsubMessage>();

    class MessageReceivedToQueue implements MessageReceiver {

        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
//            System.out.println("The MessageReceiver sees: " + message.getData().toString());
            messages.offer(message);
            consumer.ack();
        }
    }

    public GooglePubSubReader(String project,
                              String subscription,
                              GoogleCredentials credentials) {
        projectId = project;
        this.subscription = subscription;
        this.credentials = credentials;
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
                projectId, subscription);
        subscriber =
                Subscriber.newBuilder(subscriptionName, new MessageReceivedToQueue())
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();

        ExecutorService pool = Executors.newCachedThreadPool();

        subscriber.addListener(
                new Subscriber.Listener() {
                    public void failed(Subscriber.State from, Throwable failuer) {
                        System.out.println("Failure in Reader pool: " + from);
                    }
                },
        pool);
        subscriber.startAsync();
    }

    @Override
    public Message read() {
        PubsubMessage pmsg;
        if((pmsg = messages.poll()) != null) {
//            System.out.println("The read() method in the Reader sees: " + pmsg.getData().toString());
            return new Message(pmsg.getAttributesMap().get("uuid"), pmsg.getData().toStringUtf8());
        } else {
            return null;
        }
    }

    public void stop() {
        subscriber.stopAsync().awaitTerminated();
    }

    public static GooglePubSubReader.Builder fromBuilder() {
        return new GooglePubSubReader.Builder();
    }

    public static class Builder {
        private String projectId;
        private String subscription;
        private String role;

        public Builder() {
        }

        public Builder withProjectId(String id) {
            projectId = id;
            return this;
        }

        public Builder withRole(String str) {
            this.role = str;
            return this;
        }

        public Builder withSubscription(String str) {
            subscription = str;
            return this;
        }

        public GooglePubSubReader build() {
            GoogleCredentials creds = new GoogleCredentialsBuilder()
                    .withRole(role)
                    .build();
            return new GooglePubSubReader(
                    projectId,
                    subscription,
                    creds
            );
        }
    }
}
