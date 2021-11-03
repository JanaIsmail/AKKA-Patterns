package demo;

import akka.actor.ActorRef;

public class RefMessage {
	
    public final ActorRef transmitter;
    public final ActorRef destination;

    public RefMessage(ActorRef t, ActorRef d) {
    	transmitter = t;
    	destination = d;
    }
  }