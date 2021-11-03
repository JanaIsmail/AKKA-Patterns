package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.HashSet;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actor extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> references;
	private HashSet<Integer> sequences;

	public Actor() { references = new ArrayList<ActorRef>(); sequences = new HashSet<Integer>(); }
	
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
			.match(Message.class, this::receiveMessage)
			.build();
	  }

	public void receiveRef(RefMessage m){
		references = m.neighbors;
	}
	
	public void receiveMessage(Message m){
		if(sequences.add(m.seqNum)) {
			log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
			for (ActorRef ref : references) ref.tell(m, getSelf()); 
		}
	}
}
