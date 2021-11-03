package demo;

import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorB extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public ActorB() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(ActorB.class, () -> {
			return new ActorB();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.build();
	  }

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received a request from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		StringMessage r = new StringMessage("Response " + m.message.substring(m.message.length() - 1));
		getSender().tell(r, this.getSelf());
	}
}
