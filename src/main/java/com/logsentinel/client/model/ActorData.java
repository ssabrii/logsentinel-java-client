package com.logsentinel.client.model;

import java.util.List;

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
    public void setActorRoles(List<String> actorRole) {
        this.actorRoles = actorRole;
    }
    public String getActorId() {
        return actorId;
    }
    public void setActorId(String actorId) {
        this.actorId = actorId;
    }
    public String getActorDisplayName() {
        return actorDisplayName;
    }
    public void setActorDisplayName(String actorAlias) {
        this.actorDisplayName = actorAlias;
    }
}
