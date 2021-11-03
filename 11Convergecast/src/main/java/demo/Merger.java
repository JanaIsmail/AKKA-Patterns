package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Merger extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private ActorRef destination; 
	private ArrayList<ActorRef> members;
	private HashMap<ActorRef, ArrayList<String>> messages; // we store the messages sent by each actor

	public Merger() { 
		members = new ArrayList<ActorRef>(); 
		messages = new HashMap<ActorRef, ArrayList<String>>();
	}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Merger.class, () -> {
			return new Merger();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(StringMessage.class, this::receiveString)
			.match(RefMessage.class, this::receiveRef)
			.build();
	  }
	
	public void receiveRef(RefMessage m){
		log.info("["+getSelf().path().name()+"] received reference from ["+ getSender().path().name() +"] with merger: ["+m.ref.path().name() +"]");
		destination = m.ref;
	}

	public void receiveString(StringMessage m){
		log.info("["+getSelf().path().name()+"] received message from ["+ getSender().path().name() +"] with data: ["+m.message+"]");
		if(m.message.equals("join")) {
			members.add(getSender());
			messages.put(getSender(),new ArrayList<String>());
			return;
		}
		if(m.message.equals("unjoin")) {
			members.remove(getSender());
			messages.remove(getSender());
			return;
		}
		
		// when a message other than join or unjoin arrives, we add it to the messages
		messages.get(getSender()).add(m.message);
		int complete = members.size(); // to make sure that the same message was sent from each actor
		for (ActorRef a : messages.keySet()) {
			for (String s : messages.get(a)) {
				if(s.equals(m.message)) {
					complete--;
					break;
				}
			}
		}
		if(complete == 0) { // this means that each actor sent the same message once
			for (ActorRef a : messages.keySet()) {
				messages.get(a).remove(m.message); // removing the first occurrence of this message for each actor
			}
			destination.tell(m, getSelf()); // sending the message to the destination once it has been received from each actor once
		}
	}
	
}
