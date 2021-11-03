package demo;

import java.util.ArrayList;
import java.util.Arrays;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ControlledFlooding {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(Actor.createActor(), "A");
		final ActorRef b = system.actorOf(Actor.createActor(), "B");
		final ActorRef c = system.actorOf(Actor.createActor(), "C");
		final ActorRef d = system.actorOf(Actor.createActor(), "D");
		final ActorRef e = system.actorOf(Actor.createActor(), "E");
		
		ArrayList<ActorRef> to_a = new ArrayList<>(Arrays.asList(b, c));
		ArrayList<ActorRef> to_b = new ArrayList<>(Arrays.asList(d));
		ArrayList<ActorRef> to_c = new ArrayList<>(Arrays.asList(d));
		ArrayList<ActorRef> to_d = new ArrayList<>(Arrays.asList(e));
		
		ArrayList<ActorRef> to_e = new ArrayList<>(Arrays.asList(b));
		
	    a.tell(new RefMessage(to_a), ActorRef.noSender());
	    b.tell(new RefMessage(to_b), ActorRef.noSender());
	    c.tell(new RefMessage(to_c), ActorRef.noSender());
	    d.tell(new RefMessage(to_d), ActorRef.noSender());
	    e.tell(new RefMessage(to_d), ActorRef.noSender());
	    
	    a.tell(new Message("start",0) , ActorRef.noSender());
	    
		
	    // We wait 5 seconds before ending system (by default)
	    // But this is not the best solution.
	    try {
			waitBeforeTerminate();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} finally {
			system.terminate();
		}
	}
	
	public static void waitBeforeTerminate() throws InterruptedException {
		Thread.sleep(5000);
	}

}
