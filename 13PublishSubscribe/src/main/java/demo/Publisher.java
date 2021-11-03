package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;
import java.util.Random;

import akka.actor.AbstractActor;

public class Publisher extends AbstractActor {
	
	// Actor reference
	private ActorRef topic;

	public Publisher() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Publisher.class, () -> {
			return new Publisher();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(RefMessage.class, this::receiveRef)
			.match(StringMessage.class, this::receiveString)
			.build();
	  }	
	
	public void receiveRef(RefMessage m) {
		topic = m.ref;
		for (int i = 0; i < 10; i++) {
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), new StringMessage("go"), getContext().system().dispatcher(), ActorRef.noSender());
		}
	}
	
	public void receiveString(StringMessage m) {
		if (m.message.equals("go")) {
			Random random = new Random();
			String s = new String("Message" + Integer.toString(random.nextInt(100)));
			topic.tell(new StringMessage(s), getSelf());
		}
	}
}
