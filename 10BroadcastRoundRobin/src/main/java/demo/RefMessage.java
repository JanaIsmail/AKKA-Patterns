package demo;

import akka.actor.ActorRef;

public class RefMessage {
	
    public final ActorRef broadcaster;

    public RefMessage(ActorRef bc) {
    	broadcaster = bc;
    }
  }