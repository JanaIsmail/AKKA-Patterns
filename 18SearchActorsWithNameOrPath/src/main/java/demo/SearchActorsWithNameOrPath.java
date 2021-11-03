package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class SearchActorsWithNameOrPath {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(Actor.createActor(), "a");
		
		for (int i = 0; i < 5; i ++) {
			a.tell(new CreateMessage(i), ActorRef.noSender());
		}
		
		a.tell(new Search("/user/*"), ActorRef.noSender());
		a.tell(new Search("/system/*"), ActorRef.noSender());
	    
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
