package org.vashonsd;

/**
 * Loads a Server, supplied with a Publisher, Subscriber and Game, and runs it.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       Server s = Server.fromBuilder()
               .withGooglePubsub()
               .withGame(new DemoGame())
               .build();
       s.run();
    }
}
