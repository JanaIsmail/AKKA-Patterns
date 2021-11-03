package demo;

import akka.actor.Props;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Receiver2 extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Receiver2() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Receiver2.class, () -> {
			return new Receiver2();
		});
	}
	
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.build();
	  }

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
	}
}
