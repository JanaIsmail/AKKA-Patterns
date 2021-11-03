package demo;

import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

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
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
		for (int i = 0; i < 10; i++) {
			String request = "Request " + Integer.toString(i);
			RefStringMessage req = new RefStringMessage(request, m.refC);
			m.refB.tell(req, getSelf());
		}
	}
	
}
