package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class ElasticLoadBalancer {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef client = system.actorOf(Client.createActor(), "client");
		
		final ActorRef lb = system.actorOf(LoadBalancer.createActor(), "loadBalancer");
		
		lb.tell(new StringMessage("max 2"), ActorRef.noSender());

		RefMessage m = new RefMessage(lb);
	    client.tell(m, ActorRef.noSender());
		
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
