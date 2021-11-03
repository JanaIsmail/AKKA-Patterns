package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class TellToAndForget {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(ActorA.createActor(), "a");
		final ActorRef b = system.actorOf(ActorB.createActor(), "b");
		final ActorRef t = system.actorOf(Transmitter.createActor(), "t");

		RefMessage m = new RefMessage(t,b);
		StringMessage s = new StringMessage("start");
		
	    a.tell(m, ActorRef.noSender());
	    a.tell(s, ActorRef.noSender());
	    
		
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
