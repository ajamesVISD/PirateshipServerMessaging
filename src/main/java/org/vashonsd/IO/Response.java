package org.vashonsd.IO;

public class Response {

    String body;
    String uuid;

    public Response(String uuid, String body) {
        this.uuid = uuid;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Response{" +
                "body='" + body + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
