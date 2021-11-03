package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SessionManager extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// The manager keeps a record of every opened session and her client
	private HashMap<ActorRef, ActorRef> sessions;

	public SessionManager() { sessions = new HashMap<ActorRef, ActorRef>(); }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(SessionManager.class, () -> {
			return new SessionManager();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.build();
	  }

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		if(m.message.equals("createSession")) {
			final ActorRef session = getContext().getSystem().actorOf(Session.createActor(), getSender().path().name() + "Session");
			sessions.put(getSender(), session);
			getSender().tell(new RefStringMessage("session",session), getSelf());
		}
		if(m.message.equals("endSession")) {
			sessions.get(getSender()).tell(new StringMessage("stop"), ActorRef.noSender());
			sessions.remove(getSender());
		}
	}
	
}
