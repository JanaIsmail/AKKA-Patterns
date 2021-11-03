package demo;

import akka.actor.ActorRef;

public class RefStringMessage {
	
    public final String message;
    public final ActorRef ref;

    public RefStringMessage(String m, ActorRef r) {
    	message = m;
    	ref = r;
    }
  }