package com.logsentinel.enums;

import com.logsentinel.client.model.AuditLogEntry;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

/**
 * Enum that maps integer version to function that extracts hashable content from AuditLogEntry.
 */
public enum HashableContent {
    BASE(1, baseHashableContent());

    private int version;
    private Function<AuditLogEntry, String> contentExtractor;

    HashableContent(int version, Function<AuditLogEntry, String> contentExtractor) {
        this.version = version;
        this.contentExtractor = contentExtractor;
    }

    public static HashableContent fromVersion(int version) {
        for (HashableContent hc : values()) {
            if (hc.version == version) {
                return hc;
            }
        }
        throw new IllegalArgumentException("unsupported version: " + version);
    }

    public Function<AuditLogEntry, String> getContentExtractor() {
        return contentExtractor;
    }

    private static Function<AuditLogEntry, String> baseHashableContent() {
        return entry -> StringUtils.trimToEmpty(entry.getActorId())
                + StringUtils.trimToEmpty(entry.getActorRole() == null ? "" : StringUtils.join(entry.getActorRole(), ","))
                + StringUtils.trimToEmpty(entry.getAction())
                + StringUtils.trimToEmpty(entry.getEntityId()) + StringUtils.trimToEmpty(entry.getEntityType())
                + StringUtils.trimToEmpty(entry.getDetails()) + StringUtils.trimToEmpty(String.valueOf(entry.getTimestamp()))
                + StringUtils.trimToEmpty(entry.getApplicationId() == null ? "" : entry.getApplicationId().toString())
                + StringUtils.trimToEmpty(entry.getEntryType())
                + (entry.getAdditionalParams() == null ? "" : entry.getAdditionalParams().entrySet()
                    .stream()
                    .map(e -> e.getKey() + "=" + e.getValue() + "|")
                    .reduce("", String::concat));
    }

}

