package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

import akka.actor.AbstractActor;

public class Client extends AbstractActor {
	
	// Actor reference
	private ActorRef loadBalancer;
	private int requestNb;

	public Client() { requestNb = 0; }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Client.class, () -> {
			return new Client();
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
		loadBalancer = m.ref;
		for (int i = 0; i < 5; i++) {
			getContext().system().scheduler().scheduleOnce(Duration.ofMillis(1000), getSelf(), new StringMessage("go"), getContext().system().dispatcher(), ActorRef.noSender());
		}
		// This last request will arrive few seconds after the servers are stopped
		// The load balancer will create a new server to treat this request
		getContext().system().scheduler().scheduleOnce(Duration.ofMillis(3000), getSelf(), new StringMessage("go"), getContext().system().dispatcher(), ActorRef.noSender());
	}
	
	public void receiveString(StringMessage m) {
		if (m.message.equals("go")) {
			loadBalancer.tell(new StringMessage("Request" + Integer.toString(requestNb)), getSelf());
			requestNb++;
		}
	}
}
