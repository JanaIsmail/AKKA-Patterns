package demo;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Multicast {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef r1 = system.actorOf(Receiver.createActor(), "a");
		final ActorRef r2 = system.actorOf(Receiver.createActor(), "b");
		final ActorRef r3 = system.actorOf(Receiver.createActor(), "c");
		final ActorRef mult = system.actorOf(Multicaster.createActor(), "multicaster");
		
		ArrayList<ActorRef> g1 = new ArrayList<ActorRef>();
		ArrayList<ActorRef> g2 = new ArrayList<ActorRef>();
		g1.add(r1); g1.add(r2);
		g2.add(r2); g2.add(r3);
		
		Group grp1 = new Group("grp1", g1); 
		Group grp2 = new Group("grp2", g2);
		
		mult.tell(grp1, ActorRef.noSender());
		mult.tell(grp2, ActorRef.noSender());
		
		GrpMessage m1 = new GrpMessage("grp1", "Hi");
		GrpMessage m2 = new GrpMessage("grp2", "Hello");
		
		mult.tell(m1, ActorRef.noSender());
		mult.tell(m2, ActorRef.noSender());
		
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
