package demo;

import akka.actor.Props;

import java.util.ArrayList;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Topic extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> subscribers;

	public Topic() { subscribers = new ArrayList<ActorRef>(); }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Topic.class, () -> {
			return new Topic();
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
		if(m.message.equals("subscribe")) {
			subscribers.add(getSender());
			return;
		}
		if(m.message.equals("unsubscribe")) {
			subscribers.remove(getSender());
			return;
		}
		for(ActorRef a : subscribers) {
			a.tell(m, getSelf());
		}
	}	
}
