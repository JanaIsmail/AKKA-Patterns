package demo;

import akka.actor.Props;

import java.time.Duration;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

public class ActorA extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public ActorA() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(ActorA.class, () -> {
			return new ActorA();
		});
	}
	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
//			.match(StringMessage.class, this::receiveString)
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }

//	public void receiveString(StringMessage m){
//		log.info("["+getSelf().path().name()+"] received a response from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
//	}
	
	public void receiveRef(RefMessage m) throws Exception {
		for (int i = 0; i < 3; i++) {
			String request = "Request " + Integer.toString(i);
			StringMessage req = new StringMessage(request);
			
			Timeout timeout = Timeout.create(Duration.ofSeconds(5));
			Future<Object> future = Patterns.ask(m.ref, req, timeout);
			StringMessage response = (StringMessage) Await.result(future, timeout.duration());
			log.info("["+getSelf().path().name()+"] received a response from ["+ getSender().path().name() +"] with data: ["+response.message+"]");
		}
	}
	
}
