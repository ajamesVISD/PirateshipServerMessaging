package org.vashonsd.IO.Service;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.pubsub.v1.*;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.*;
import org.vashonsd.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class GooglePubSubWriter implements Writer {

    private String projectId;

    private final Map<String,Publisher> publishers = new HashMap<>();

    private final CredentialsProvider credentialsProvider;
    private TopicAdminClient topicAdminClient;
    private SubscriptionAdminClient subscriptionAdminClient;

    List<ApiFuture<String>> messageIdFutures = new ArrayList<>();

    public void setTopicAdminClient(TopicAdminClient topicAdminClient) {
        this.topicAdminClient = topicAdminClient;
    }

    public GooglePubSubWriter(String projectId,
                              String role) {

        this.projectId = projectId;

        //Create our credentials
        GoogleCredentials credentials = new GoogleCredentialsBuilder()
                .withRole(role)
                .build();
        credentialsProvider = FixedCredentialsProvider.create(credentials);

        //Make a project admin client from the credentials.
        try {
            topicAdminClient = TopicAdminClient.create(TopicAdminSettings.newBuilder()
                    .setCredentialsProvider(credentialsProvider)
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Make a subscription admin client from the credentials.
        try {
            subscriptionAdminClient = SubscriptionAdminClient.create(SubscriptionAdminSettings
                    .newBuilder()
                    .setCredentialsProvider(credentialsProvider)
                    .build());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void write(String topic, Message msg) {
        PubsubMessage pmsg = PubsubMessage
                .newBuilder()
                .setData(ByteString.copyFromUtf8(msg.getBody()))
                .putAttributes("uuid", msg.getUuid())
                .build();


        //If we have never met this project before, we are going to add it to our map of project:Publisher.
        Publisher publisher;
        if((publisher = publishers.get(topic)) == null) {
            publisher = setUpTopicAndSubscription(topic);
        }

        try {
//            System.out.println("Server>Publisher>write: " + pmsg.toString() + " to topic " + topic);
            ApiFuture<String> messageIdFuture = publisher.publish(pmsg);
            messageIdFutures.add(messageIdFuture);
        } finally {
            List<String> messageIds = null;
            try {
                messageIds = ApiFutures.allAsList(messageIdFutures).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

//            for (String messageId : messageIds) {
//                System.out.println("published with message ID: " + messageId);
//            }
        }
    }

    public Publisher setUpTopicAndSubscription(String topic) {

        ProjectTopicName projectTopicName = ProjectTopicName.of(projectId, topic);

            //Now we create a project, create an entry in our Map, and create a publisher to go with the project.
            //Sure would be nice if Google's API included a .contains(project) method...
            Iterable<Topic> topicsList = topicAdminClient.listTopics(ProjectName.of(projectId)).iterateAll();
            boolean present = false;
            for(Topic t : topicsList) {
                if (t.getName().equals(projectTopicName.toString())) {
                    present = true;
                    break;
                }
            }

            Topic thisTopic;

            if(!present) {
                thisTopic = topicAdminClient.createTopic(projectTopicName);
            } else {
                thisTopic = topicAdminClient.getTopic(projectTopicName);
            }

            //We also have to create a subscription named after the project.
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

            Subscription thisSubscription;

            if(!present) {
                thisSubscription = subscriptionAdminClient.createSubscription(
                        Subscription.newBuilder()
                                .setName(projectSubscriptionName.toString())
                                .setTopic(projectTopicName.toString())
                                .setPushConfig(PushConfig.getDefaultInstance())
                                .build()
                );
            } else {
                thisSubscription = subscriptionAdminClient.getSubscription(projectSubscriptionName);
            }

            boolean ready = false;
            while(!ready) {
                ready = (thisTopic.isInitialized() && thisSubscription.isInitialized());
            }

            //Now we give our map a new Publisher to match the project + subscription.

        Publisher publisher;
        try {
            publisher = Publisher.newBuilder(projectTopicName)
                    .setCredentialsProvider(credentialsProvider)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
            publisher = null;
        }

        publishers.put(topic, publisher);
        return publisher;
    }

    public static Builder fromBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String role;
        private String project;

        public Builder() {
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Builder withProject(String project) {
            this.project = project;
            return this;
        }

        public GooglePubSubWriter build() {
            return new GooglePubSubWriter(project, role);
        }
    }
}
