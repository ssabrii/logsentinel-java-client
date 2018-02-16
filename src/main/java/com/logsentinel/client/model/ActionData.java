package com.logsentinel.client.model;

import org.javers.core.diff.Diff;

public class ActionData<T> {

    private String action;
    private String entityId;
    private String entityType;
    private T details;
    private Diff diffDetails;
    
    private AuditLogEntryType entryType;
    
    public ActionData(T details) {
        this.details = details;
    }
    
    public String getAction() {
        return action;
    }
    public ActionData<T> setAction(String action) {
        this.action = action;
        return this;
    }
    public String getEntityId() {
        return entityId;
    }
    public ActionData<T> setEntityId(String entityId) {
        this.entityId = entityId;
        return this;
    }
    public String getEntityType() {
        return entityType;
    }
    public ActionData<T> setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }
    public T getDetails() {
        return details;
    }
    public ActionData<T> setDetails(T details) {
        this.details = details;
        return this;
    }

    public AuditLogEntryType getEntryType() {
        return entryType;
    }

    public ActionData<T> setEntryType(AuditLogEntryType entryType) {
        this.entryType = entryType;
        return this;
    }

	public Diff getDiffDetails() {
		return diffDetails;
	}

	public ActionData<T> setDiffDetails(Diff diffDetails) {
		this.diffDetails = diffDetails;
		return this;
	}
    
    
}
