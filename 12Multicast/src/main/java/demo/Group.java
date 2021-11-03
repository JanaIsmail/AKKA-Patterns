package demo;

import java.io.Serializable;
import java.util.ArrayList;

import akka.actor.ActorRef;

public class Group implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5L;
	public final String groupName;
    public final ArrayList<ActorRef> members;

    public Group(String gn, ArrayList<ActorRef> m) {
    	groupName = gn;
    	members = m; // did not work: org.apache.commons.lang3.SerializationUtils.clone(m);
    }
  }