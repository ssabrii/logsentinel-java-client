package com.logsentinel.client.model;

import java.util.List;

/**
 * Data about the actor who performed an action
 *
 */
public class ActorData {

    private String actorId;
    private List<String> actorRoles;
    private String actorDisplayName;

    public ActorData(String actorId) {
        this.actorId = actorId;
    }
    
    public List<String> getActorRoles() {
        return actorRoles;
    }
    
    /**
     * Sets the roles of the actor (optional)
     * 
     * @param actorRoles a list of roles
     */
    public ActorData setActorRoles(List<String> actorRoles) {
        this.actorRoles = actorRoles;
        return this;
    }
    
    public String getActorId() {
        return actorId;
    }
    
    /**
     * Sets the ID of the actor
     * 
     * @param actorId
     */
    public ActorData setActorId(String actorId) {
        this.actorId = actorId;
        return this;
    }
    public String getActorDisplayName() {
        return actorDisplayName;
    }
    
    /**
     * Sets a display name for the actor (optional)
     * 
     * @param actorDisplayName
     */
    public ActorData setActorDisplayName(String actorDisplayName) {
        this.actorDisplayName = actorDisplayName;
        return this;
    }
}
