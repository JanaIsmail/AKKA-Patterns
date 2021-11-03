package demo;

import java.time.Duration;
import static akka.pattern.Patterns.gracefulStop;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.AskTimeoutException;

public class StoppingActors {

	public static void main(String[] args) throws Exception {
		
		final ActorSystem system = ActorSystem.create("system");

		final ActorRef stop = system.actorOf(Stop.createActor(), "stop_example");
		final ActorRef child = system.actorOf(Stop.createActor(), "child");
		final ActorRef poison_pill = system.actorOf(PoisonPill.createActor(), "poison_pill_example");
		final ActorRef kill = system.actorOf(Kill.createActor(), "kill_example");
		final ActorRef graceful = system.actorOf(GracefulStop.createActor(), "graceful_stop_example");
		
		RefMessage r = new RefMessage(child);
	    stop.tell(r, ActorRef.noSender());
	    
	    // Stop
//	    for(int i = 0; i < 20; i++) {
//	    	stop.tell(new StringMessage("Message" + Integer.toString(i)), ActorRef.noSender());
//	    	child.tell(new StringMessage("Message" + Integer.toString(i)), ActorRef.noSender());
//	    	if(i == 12) stop.tell(new StringMessage("stop child"), ActorRef.noSender());
//	    	if(i == 17) stop.tell(new StringMessage("stop"), ActorRef.noSender());
//	    }
	    
	    
	    // Poison Pill
//	    for(int i = 0; i < 20; i++) {
//	    	poison_pill.tell(new StringMessage("Message" + Integer.toString(i)), ActorRef.noSender());
//	    	if(i == 12) poison_pill.tell(akka.actor.PoisonPill.getInstance(), ActorRef.noSender());
//	    }
	    
	    
	    // Kill
//	    for(int i = 0; i < 20; i++) {
//	    	kill.tell(new StringMessage("Message" + Integer.toString(i)), ActorRef.noSender());
//	    	if(i == 12) kill.tell(akka.actor.Kill.getInstance(), ActorRef.noSender());
//	    }
	    
	    
	    // Graceful Stop
//	    for(int i = 0; i < 20; i++) {
//	    	graceful.tell(new StringMessage("Message" + Integer.toString(i)), ActorRef.noSender());
//	    	if(i == 12) {
//	    		try {
//	    			  CompletionStage<Boolean> stopped = gracefulStop(graceful, Duration.ofSeconds(5), "Actor graceful stopped");
//	    			  stopped.toCompletableFuture().get(6, TimeUnit.SECONDS);
//	    			} catch (AskTimeoutException e) { e.printStackTrace(); }
//	    	}
//	    }
		
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
