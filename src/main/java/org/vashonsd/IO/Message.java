package org.vashonsd.IO;

/**
 * Created by andy on 6/2/18.
 */
public class Message {
    String uuid;
    String body;

    public Message(String uuid, String body) {
        this.uuid = uuid;
        this.body = body;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid='" + uuid + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
