package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;

public class RefMessage {
	
	public ArrayList<ActorRef> neighbors;
	
	public RefMessage(ArrayList<ActorRef> n) {
		neighbors = n;
	}
}

