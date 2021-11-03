package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorA extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef merger;
	private int counter;

	public ActorA() { counter = 0; }
	
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
		//log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		if(m.message.equals("go")) {
			String message = new String("Hi" + Integer.toString(counter));
			merger.tell(new StringMessage(message), getSelf());
			counter++;
		}
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received reference from ["+ getSender().path().name() +"] with merger: ["+m.ref.path().name() +"]");
		merger = m.ref;
		merger.tell(new StringMessage("join"), getSelf());
		for(int i = 0; i <= 10; i++) {
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), new StringMessage("go"), getContext().system().dispatcher(), ActorRef.noSender());
		}
	}
}
