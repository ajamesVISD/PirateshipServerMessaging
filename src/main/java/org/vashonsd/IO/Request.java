package org.vashonsd.IO;

public class Request {
    String uuid;
    String body;

    public Request(String uuid, String body) {
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
}
