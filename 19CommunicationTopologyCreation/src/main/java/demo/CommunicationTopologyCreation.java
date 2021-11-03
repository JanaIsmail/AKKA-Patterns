package demo;

import java.util.ArrayList;
import java.util.Arrays;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class CommunicationTopologyCreation {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(Actor.createActor(), "1");
		final ActorRef b = system.actorOf(Actor.createActor(), "2");
		final ActorRef c = system.actorOf(Actor.createActor(), "3");
		final ActorRef d = system.actorOf(Actor.createActor(), "4");
		
		ArrayList<ActorRef> to_a = new ArrayList<>(Arrays.asList(b, c));
		ArrayList<ActorRef> to_b = new ArrayList<>(Arrays.asList(d));
		ArrayList<ActorRef> to_c = new ArrayList<>(Arrays.asList(a, d));
		ArrayList<ActorRef> to_d = new ArrayList<>(Arrays.asList(a, d));
		
	    a.tell(new RefMessage(to_a), ActorRef.noSender());
	    b.tell(new RefMessage(to_b), ActorRef.noSender());
	    c.tell(new RefMessage(to_c), ActorRef.noSender());
	    d.tell(new RefMessage(to_d), ActorRef.noSender());
	    
		
	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			system.terminate();
		}
	}
	
	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
