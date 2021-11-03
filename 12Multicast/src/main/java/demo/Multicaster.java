package demo;

import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.HashMap;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Multicaster extends AbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	// Actor reference
	private HashMap<String, ArrayList<ActorRef>> groups;

	public Multicaster() { groups = new HashMap<String, ArrayList<ActorRef>>(); }
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Multicaster.class, () -> {
			return new Multicaster();
		});
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
			.match(Group.class, this::receiveGroup)
			.match(GrpMessage.class, this::receiveGrpString)
			.build();
	  }

	public void receiveGrpString(GrpMessage m){
		log.info("["+getSelf().path().name()+"] received multicast from ["+ getSender().path().name() +"] to group: ["+m.groupName+"] with message: ["+m.message+"]");
		for (ActorRef act : groups.get(m.groupName)) {
		    act.tell(new StringMessage(m.message), getSelf());
		}
	}
	
	public void receiveGroup(Group m){
		log.info("["+getSelf().path().name()+"] received new group from ["+ getSender().path().name() +"] with name: ["+m.groupName+"]");
		groups.put(m.groupName, m.members);
	}
	
}
