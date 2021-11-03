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
	private ActorRef destination;
	private ActorRef transmitter;

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
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		if(m.message.equals("start")) {
			RefStringMessage r = new RefStringMessage("hello",destination);
			transmitter.tell(r, this.getSelf());
		}
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"]");
		transmitter = m.transmitter;
		destination = m.destination;
	}
	
}
