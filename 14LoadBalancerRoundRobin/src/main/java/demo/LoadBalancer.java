package demo;

import akka.actor.Props;

import java.util.ArrayList;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class LoadBalancer extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ArrayList<ActorRef> servers;
	private int turn;

	public LoadBalancer() { servers = new ArrayList<ActorRef>(); turn = 0; }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(LoadBalancer.class, () -> {
			return new LoadBalancer();
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
		if(m.message.equals("join")) {
			servers.add(getSender());
			return;
		}
		if(m.message.equals("unjoin")) {
			servers.remove(getSender());
			return;
		}
		servers.get(turn%servers.size()).tell(m, getSelf());
		turn++;
	}	
}
