package com.logsentinel.client.model;

import java.util.Map;

public class BatchLogRequestEntry {
    
    private ActorData actorData;
    private ActionData actionData;
    private Map<String, String> additionalParams;
    
    public ActorData getActorData() {
        return actorData;
    }
    public void setActorData(ActorData actorData) {
        this.actorData = actorData;
    }
    public ActionData getActionData() {
        return actionData;
    }
    public void setActionData(ActionData actionData) {
        this.actionData = actionData;
    }
    public Map<String, String> getAdditionalParams() {
        return additionalParams;
    }
    public void setAdditionalParams(Map<String, String> additionalParams) {
        this.additionalParams = additionalParams;
    }
}
