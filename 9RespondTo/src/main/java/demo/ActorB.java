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
			.match(RefStringMessage.class, this::receiveRefString)
			.build();
	  }

	public void receiveRefString(RefStringMessage m){
		log.info("["+getSelf().path().name()+"] received request from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		String request = m.message;
		String response = "Response " + request.substring(request.length() - 1);
		StringMessage r = new StringMessage(response);
		m.ref.tell(r, this.getSelf());
	}
}
