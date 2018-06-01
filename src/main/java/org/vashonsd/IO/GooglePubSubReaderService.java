package org.vashonsd.IO;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * The ReaderService pulls from the correct Pub/Sub project + subscription and puts what it gets on the queue.
 */
public class GooglePubSubReaderService implements MessageReaderService {

    private final String projectId;

    private final String subscription;
    private final Subscriber subscriber;
    GoogleCredentials credentials;
    final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<PubsubMessage>();

    class MessageReceiverExample implements MessageReceiver {

        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
            messages.offer(message);
//            System.out.println("The callback got: " + message.toString());
            consumer.ack();
        }
    }

    public GooglePubSubReaderService(String role,
                                     String project,
                                     String subscription,
                                     GoogleCredentials credentials) {
        projectId = project;
        this.subscription = subscription;
        this.credentials = credentials;
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(
                projectId, subscription);
        subscriber =
                Subscriber.newBuilder(subscriptionName, new MessageReceiverExample())
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        subscriber.startAsync().awaitRunning();
    }

    @Override
    public Request pullRequests() {
        PubsubMessage pmsg;
//        System.out.println("pullRequest made");
        if((pmsg = messages.poll()) != null) {
//            System.out.println("The in-service message queue got: " + pmsg.getData().toStringUtf8());
            return new Request(pmsg.getAttributesMap().get("uuid"), pmsg.getData().toStringUtf8());
        } else {
            return null;
        }
    }

    public static GooglePubSubReaderBuilder fromBuilder() {
        return new GooglePubSubReaderBuilder();
    }

    public boolean setRole() {
        return true;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getSubscription() {
        return subscription;
    }
}
