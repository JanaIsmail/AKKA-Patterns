package demo;

import akka.actor.Props;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Server extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Server() { }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Server.class, () -> {
			return new Server();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.build();
	  }

	public void receiveString(StringMessage m){
		if(m.message.equals("stop")) {
			getContext().stop(getSelf());
		}
		log.info("["+getSelf().path().name()+"] received request from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		getSender().tell(new StringMessage(m.message + " finished"), getSender());
	}
}
