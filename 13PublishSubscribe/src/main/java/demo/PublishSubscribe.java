package demo;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class PublishSubscribe {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef a = system.actorOf(Subscriber.createActor(), "a");
		final ActorRef b = system.actorOf(Subscriber.createActor(), "b");
		final ActorRef c = system.actorOf(Subscriber.createActor(), "c");
		
		final ActorRef topic1 = system.actorOf(Topic.createActor(), "Topic1");
		final ActorRef topic2 = system.actorOf(Topic.createActor(), "Topic2");
		
		final ActorRef p1 = system.actorOf(Publisher.createActor(), "Publisher1");
		final ActorRef p2 = system.actorOf(Publisher.createActor(), "Publisher2");
		
		RefMessage t1 = new RefMessage(topic1);
		RefMessage t2 = new RefMessage(topic2);
		
	    a.tell(t1, ActorRef.noSender());
	    b.tell(t1, ActorRef.noSender());
	    c.tell(t1, ActorRef.noSender());
	    
	    a.tell(t2, ActorRef.noSender());
	    b.tell(t2, ActorRef.noSender());
	    c.tell(t2, ActorRef.noSender());
	    
	    p1.tell(t1, ActorRef.noSender());
	    p2.tell(t2, ActorRef.noSender());
		
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
