package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Random;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Subscriber extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	// The subscriber has a reference to every topic
	// At any moment he can choose to subscribe to any of them
	private HashMap<ActorRef, Boolean> topics;

	public Subscriber() { topics = new HashMap<ActorRef, Boolean>(); }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Subscriber.class, () -> {
			return new Subscriber();
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
		// the subscriber chooses randomly to change subscription or do nothing
		Random random = new Random();
		if(random.nextBoolean()) { // chooses to subscribe or unsubscribe to the topic n
			for (ActorRef a : topics.keySet()) {
				if (topics.get(a) && random.nextBoolean()) {
					a.tell(new StringMessage("unsubscribe"), getSelf());
					topics.put(a, false);
					break;
				}
				else if (!topics.get(a) && random.nextBoolean()) {
					a.tell(new StringMessage("subscribe"), getSelf());
					topics.put(a, true);
					break;
				}
			}
		}
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received topic from ["+ getSender().path().name() +"] with name: ["+m.ref.path().name() +"]");
		// the subscriber chooses randomly to subscribe or not
		Random random = new Random();
		if(random.nextBoolean()) {
			topics.put(m.ref, true);
			m.ref.tell(new StringMessage("subscribe"), getSelf());
		}
		else {
			topics.put(m.ref, false);
		}
	}
}
