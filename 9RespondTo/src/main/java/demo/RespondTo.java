package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class RespondTo {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");
		
		final ActorRef a = system.actorOf(ActorA.createActor(), "a");
		final ActorRef b = system.actorOf(ActorB.createActor(), "b");
		final ActorRef c = system.actorOf(ActorC.createActor(), "c");

		RefMessage m = new RefMessage(b,c);
		
	    a.tell(m, ActorRef.noSender());
		
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
