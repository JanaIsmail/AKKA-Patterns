package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Convergecast {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef a = system.actorOf(ActorA.createActor(), "a");
		final ActorRef b = system.actorOf(ActorB.createActor(), "b");
		final ActorRef c = system.actorOf(ActorC.createActor(), "c");
		final ActorRef destination = system.actorOf(ActorD.createActor(), "d");
		final ActorRef merger = system.actorOf(Merger.createActor(), "merger");
		
		RefMessage m = new RefMessage(merger);
		RefMessage dest = new RefMessage(destination);
		
	    a.tell(m, ActorRef.noSender());
	    b.tell(m, ActorRef.noSender());
	    c.tell(m, ActorRef.noSender());
	    
	    merger.tell(dest, ActorRef.noSender());
		
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
