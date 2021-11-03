package demo;

import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorC extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public ActorC() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(ActorC.class, () -> {
			return new ActorC();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.build();
	  }

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received response from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
	}
	
}
