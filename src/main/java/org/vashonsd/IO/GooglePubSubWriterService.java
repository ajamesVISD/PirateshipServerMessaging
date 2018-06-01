package org.vashonsd.IO;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.StreamSupport;

public class GooglePubSubWriterService implements MessageWriterService {

    private final CredentialsProvider credentialsProvider;
    private String projectId;
    private Subscriber subscriber;

    private final Map<String,Publisher> publishers = new HashMap<String, Publisher>();

    private TopicAdminClient topicAdminClient;
    private SubscriptionAdminClient subscriptionAdminClient;

    public void setTopicAdminClient(TopicAdminClient topicAdminClient) {
        this.topicAdminClient = topicAdminClient;
    }

    public GooglePubSubWriterService(String projectId,
                                     TopicAdminClient topicAdminClient,
                                     SubscriptionAdminClient subscriptionAdminClient,
                                     CredentialsProvider credentialsProvider) {
        this.projectId = projectId;
        this.topicAdminClient = topicAdminClient;
        this.subscriptionAdminClient = subscriptionAdminClient;
        this.credentialsProvider = credentialsProvider;
    }

    @Override
    public void pushResponse(String topic, Response resp) {
        Publisher publisher;
        PubsubMessage pmsg = PubsubMessage
                .newBuilder()
                .setData(ByteString.copyFromUtf8(resp.getBody()))
                .putAttributes("uuid", resp.getUuid())
                .build();

        ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, topic);
        if(!publishers.containsKey(topic)) {
            //Now we create a topic, create an entry in our Map, and create a publisher to go with the topic.
            Iterable<Topic> topicsList = topicAdminClient.listTopics(ProjectName.of(projectId)).iterateAll();
            boolean present = false;
            for(Topic t : topicsList) {
//                System.out.println(t.getName());
                if (t.getName().equals(projectTopicName.toString())) {
                    present = true;
//                    System.out.println("found it");
                    break;
                }
            }
            if(!present) {
                topicAdminClient.createTopic(projectTopicName);
            }

            ProjectSubscriptionName projectSubscriptionName = ProjectSubscriptionName.of(projectId, topic);
            Iterable<Subscription> subs = subscriptionAdminClient.listSubscriptions(ProjectName.of(projectId)).iterateAll();
            present = false;
            for(Subscription s : subs) {
                if(s.getName().equals(projectSubscriptionName.toString())) {
//                    System.out.println("found subscription");
                    present = true;
                    break;
                }
            }
            if(!present) {
                subscriptionAdminClient.createSubscription(
                        Subscription.newBuilder()
                                .setName(projectSubscriptionName.toString())
                                .setTopic(projectTopicName.toString())
                                .setPushConfig(PushConfig.getDefaultInstance())
                                .build()
                );
            }

            try {
                publishers.put(topic, Publisher.newBuilder(projectTopicName)
                            .setCredentialsProvider(credentialsProvider)
                        .build()
                );
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        publishers.get(topic).publish(pmsg);
    }
}
