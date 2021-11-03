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
	private int max;
	private ArrayList<ActorRef> servers;
	private int turn;
	private int count_messages;

	public LoadBalancer() { servers = new ArrayList<ActorRef>(); turn = 0; count_messages = 0; }
	
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
		if(m.message.contains("max")) {
			max = Integer.parseInt(m.message.substring(m.message.length() - 1));
			return;
		}
		if(m.message.contains("finished")) {
			count_messages--;
			if (count_messages == 0) { // if there is no more messages
			// an alternative would be to make a messages counter for each server (so we have a map of servers and counters)
			// The idea would be to check if each server is still treating requests, and stop the ones who have 0 messages
			// This idea is a little complicated because we don't have indexing in a map (we can't do map[turn])
			// We need the indexing to know which server's turn it is to treat the request
				for (ActorRef a : servers) {
					a.tell(new StringMessage("stop"), ActorRef.noSender());
				}
				servers.clear();
			}
			return;
		}
		if(servers.size() < max) { // if we can still add a server
			final ActorRef server = getContext().getSystem().actorOf(Server.createActor(), "Server" + Integer.toString(servers.size()));
			servers.add(server);
		}
		servers.get(turn%servers.size()).tell(m, getSelf());
		count_messages++;
		turn++;
	}	
}
