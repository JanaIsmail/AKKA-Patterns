package demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorA extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef ref;

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
			.match(StringMessage.class, this::receiveString)
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received a response from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
		ref = m.ref;
		for (int i = 0; i < 10; i++) {
			String request = "Request " + Integer.toString(i);
			StringMessage req = new StringMessage(request);
			ref.tell(req, getSelf());
		}
	}
	
}
