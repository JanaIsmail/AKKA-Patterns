package demo;

import akka.actor.ActorRef;

public class RefMessage {
	
    public final ActorRef ref;

    public RefMessage(ActorRef r) {
    	ref = r;
    }
  }