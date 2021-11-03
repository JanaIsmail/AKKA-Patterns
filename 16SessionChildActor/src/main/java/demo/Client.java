package demo;

import akka.actor.Props;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Client extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	ActorRef manager;
	ActorRef session;

	public Client() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Client.class, () -> {
			return new Client();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(RefStringMessage.class, this::receiveRefString)
			.match(StringMessage.class, this::receiveString)
			.build();
	  }
	
	public void receiveRefString(RefStringMessage m){
		if(m.message.equals("manager")) {
			manager = m.ref;
			manager.tell(new StringMessage("createSession"), getSelf());
		}
		else if (m.message.equals("session")) {
			log.info("["+getSelf().path().name()+"] received session from ["+ getSender().path().name() +"] with name: ["+m.ref.path().name() +"]");
			session = m.ref;
			for (int i = 0; i < 10; i++) {
				session.tell(new StringMessage("Message " + Integer.toString(i)), getSelf());
			}
			manager.tell(new StringMessage("endSession"), getSelf());
			session = null;
		}
	}

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
	}
}
