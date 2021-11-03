package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class RequestDontWaitForResponse {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(ActorA.createActor(), "a");
		final ActorRef b = system.actorOf(ActorB.createActor(), "b");

		RefMessage m = new RefMessage(b);
		
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
