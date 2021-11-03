package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorSender extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef bc;

	public ActorSender() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(ActorSender.class, () -> {
			return new ActorSender();
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
		if(m.message.equals("go")) {
			StringMessage r = new StringMessage("This is a broadcast message");
			bc.tell(r, getSelf());
		}
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received reference from ["+ getSender().path().name() +"] with broadcaster: ["+m.broadcaster.path().name() +"]");
		bc = m.broadcaster;
		getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), new StringMessage("go"), getContext().system().dispatcher(), ActorRef.noSender());
	}
	
}
