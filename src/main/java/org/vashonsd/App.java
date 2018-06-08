package org.vashonsd;

/**
 * Loads a game and runs it.
 *
 */
public class App 
{
    public static void main( String[] args )
    {
       Server g = Server.fromBuilder()
               .withGooglePubsub()
               .withGame(new DemoGame())
               .build();
       g.run();
    }
}
