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
    private String department;

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
     * @return the current ActorData
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
     * @param actorId actor ID
     * @return the current ActorData
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
     * @param actorDisplayName Actor display name
     * @return the current ActorData
     */
    public ActorData setActorDisplayName(String actorDisplayName) {
        this.actorDisplayName = actorDisplayName;
        return this;
    }

    public String getDepartment() {
        return department;
    }

    /**
     * Sets actor's department
     *
     * @param department
     * @return the current ActorData
     */
    public ActorData setDepartment(String department) {
        this.department = department;
        return this;
    }
}
