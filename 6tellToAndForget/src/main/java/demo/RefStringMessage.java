package demo;

import akka.actor.ActorRef;

public class RefStringMessage {
	
    public final String message;
    public final ActorRef destination;

    public RefStringMessage(String m, ActorRef d) {
    	message = m;
    	destination = d;
    }
  }