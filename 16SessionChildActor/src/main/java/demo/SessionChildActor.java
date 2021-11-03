package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class SessionChildActor {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef client1 = system.actorOf(Client.createActor(), "client1");
		final ActorRef client2 = system.actorOf(Client.createActor(), "client2");
		final ActorRef client3 = system.actorOf(Client.createActor(), "client3");
		
		final ActorRef manager = system.actorOf(SessionManager.createActor(), "manager");

		RefStringMessage m = new RefStringMessage("manager", manager);
		
	    client1.tell(m, ActorRef.noSender());
	    client2.tell(m, ActorRef.noSender());
	    client3.tell(m, ActorRef.noSender());
		
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
