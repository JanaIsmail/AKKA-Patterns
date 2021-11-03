package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Broadcaster extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> members;

	public Broadcaster() { members = new ArrayList<ActorRef>(); }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Broadcaster.class, () -> {
			return new Broadcaster();
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
		if(m.message.equals("join")) {
			members.add(getSender());
			return;
		}
		for (ActorRef act : members) {
		    act.tell(m, getSender());
		}
	}
	
}
