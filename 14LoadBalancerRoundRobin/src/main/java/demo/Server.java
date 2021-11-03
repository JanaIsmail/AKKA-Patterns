package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Random;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Server extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	// The server has a reference to the loadBalancer
	// At any moment he can choose to join or unjoin
	private ActorRef loadBalancer;
	private Boolean joined;
	

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
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }

	public void receiveString(StringMessage m){
		// the server chooses randomly to join or unjoin
		Random random = new Random(); // true means the state changes
		if(random.nextBoolean()) {
			if(joined) loadBalancer.tell(new StringMessage("unjoin"), getSelf());
			else loadBalancer.tell(new StringMessage("join"), getSelf());
		}
		log.info("["+getSelf().path().name()+"] received request from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
	}
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received load balancer from ["+ getSender().path().name() +"] with name: ["+m.ref.path().name() +"]");
		loadBalancer = m.ref;
		joined = true;
		loadBalancer.tell(new StringMessage("join"), getSelf());
	}
}
