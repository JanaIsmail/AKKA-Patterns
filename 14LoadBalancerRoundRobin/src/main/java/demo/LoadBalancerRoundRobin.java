package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class LoadBalancerRoundRobin {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(Server.createActor(), "a");
		final ActorRef b = system.actorOf(Server.createActor(), "b");
		final ActorRef c = system.actorOf(Server.createActor(), "c");
		final ActorRef d = system.actorOf(Server.createActor(), "d");
		
		final ActorRef loadBalancer = system.actorOf(LoadBalancer.createActor(), "LB");
		
		final ActorRef client = system.actorOf(Client.createActor(), "Client");
		
		RefMessage lb = new RefMessage(loadBalancer);
		
		// All servers have access to the load balancer
		// They can join and unjoin at any moment
	    a.tell(lb, ActorRef.noSender());
	    b.tell(lb, ActorRef.noSender());
	    c.tell(lb, ActorRef.noSender());
	    d.tell(lb, ActorRef.noSender());
	    
	    // The client have a reference to the load balancer
	    // He will send all of his requests to this reference
	    client.tell(lb, ActorRef.noSender());
		
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
