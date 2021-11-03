package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actor extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> references;

	public Actor() { references = new ArrayList<ActorRef>(); }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actor.class, () -> {
			return new Actor();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }

	public void receiveRef(RefMessage m){
		references = m.neighbors;
		for (ActorRef a : references) {
			log.info("["+getSelf().path().name()+"] received reference with name: ["+a.path().name()+"]");
		}
	}
}
