package demo;

import akka.actor.ActorRef;

public class RefMessage {
	
    public final ActorRef refB;
    public final ActorRef refC;

    public RefMessage(ActorRef b, ActorRef c) {
    	refB = b;
    	refC = c;
    }
  }