package demo;

import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorMember extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	ActorRef bc;

	public ActorMember() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(ActorMember.class, () -> {
			return new ActorMember();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(RefMessage.class, this::receiveRef)
			.match(StringMessage.class, this::receiveString)
			.build();
	  }
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received reference from ["+ getSender().path().name() +"] with broadcaster: ["+m.broadcaster.path().name() +"]");
		bc = m.broadcaster;
		StringMessage r = new StringMessage("join");
		bc.tell(r, this.getSelf());
	}

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
	}
}
