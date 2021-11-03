package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class ActorC extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef merger;
	private int counter;

	public ActorC() { counter = 0; }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(ActorC.class, () -> {
			return new ActorC();
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
		if(m.message.equals("go") && counter < 6) { // C will send 5 messages to the merger then stop
			String message = new String("Hi" + Integer.toString(counter));
			merger.tell(new StringMessage(message), getSelf());
			if(counter == 5) {
				merger.tell(new StringMessage("unjoin"), getSelf());
			}
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
