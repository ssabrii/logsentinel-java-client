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
    public ActorData setActorRoles(List<String> actorRole) {
        this.actorRoles = actorRole;
        return this;
    }
    public String getActorId() {
        return actorId;
    }
    public ActorData setActorId(String actorId) {
        this.actorId = actorId;
        return this;
    }
    public String getActorDisplayName() {
        return actorDisplayName;
    }
    public ActorData setActorDisplayName(String actorAlias) {
        this.actorDisplayName = actorAlias;
        return this;
    }
}
