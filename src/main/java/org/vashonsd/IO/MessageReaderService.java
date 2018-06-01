package org.vashonsd.IO;

public interface MessageReaderService {

    /**
     * Poll the external messaging service, fetch a message, and return it formatted as a Request.
     * @return
     */
    Request pullRequests();
}
