package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Transmitter extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef source;
	private ActorRef destination;
	private String message;

	public Transmitter() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Transmitter.class, () -> {
			return new Transmitter();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(RefStringMessage.class, this::receiveRefString)
			.build();
	  }

	public void receiveRefString(RefStringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		source = getSender();
		destination = m.destination;
		message = m.message;
		StringMessage r = new StringMessage(message);
		destination.tell(r, source);
	}
	
}
