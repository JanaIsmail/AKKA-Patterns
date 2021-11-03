package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class BroadcastRoundRobin {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(ActorSender.createActor(), "a");
		final ActorRef b = system.actorOf(ActorMember.createActor(), "b");
		final ActorRef c = system.actorOf(ActorMember.createActor(), "c");
		final ActorRef bc = system.actorOf(Broadcaster.createActor(), "bc");

		RefMessage m = new RefMessage(bc);
		
	    a.tell(m, ActorRef.noSender());
	    b.tell(m, ActorRef.noSender());
	    c.tell(m, ActorRef.noSender());
		
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
