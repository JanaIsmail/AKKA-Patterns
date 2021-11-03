package demo;

import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import akka.actor.ActorIdentity;
import akka.actor.ActorSelection;
import akka.actor.Identify;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Actor extends UntypedAbstractActor {
	
	// Logger attached to actor
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

	public Actor() {}
	
	// Static function creating actor
	public static Props createActor() {
		return Props.create(Actor.class, () -> {
			return new Actor();
		});
	}
	
	public void create(CreateMessage m){
		getContext().getSystem().actorOf(Actor.createActor(), "actor" + Integer.toString(m.id));
		log.info("Actor" + Integer.toString(m.id) + " created");
	}
	
	public void search(Search s){
		ActorSelection selection = getContext().actorSelection(s.name);
		selection.tell(new Identify(1), getSelf());
	}
	
	public void identify(ActorIdentity i){
		log.info("Actor " + i.getActorRef().get().path().name() + " identified");
	}
	
	@Override
	public void onReceive(Object message) throws Throwable {
		if(message instanceof CreateMessage) {
			CreateMessage m = (CreateMessage) message;
			create(m); 
		}
		if(message instanceof Search) { 
			Search m = (Search) message;
			search(m);
		}
		if(message instanceof ActorIdentity) { 
			ActorIdentity m = (ActorIdentity) message;
			identify(m);
		}
	}
}
