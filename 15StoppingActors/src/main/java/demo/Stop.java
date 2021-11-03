package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Stop extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef child;

	public Stop() { }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Stop.class, () -> {
			return new Stop();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }
	
	@Override
	public void postStop() {
		log.info("["+getSelf().path().name()+"] stopped");
	}
	

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		if(m.message.equals("stop")) {
			getContext().stop(getSelf());
		}
		else if(m.message.equals("stop child")) {
			getContext().stop(child);
		}
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received child from ["+ getSender().path().name() +"]");
		child = m.ref;
	}
}
