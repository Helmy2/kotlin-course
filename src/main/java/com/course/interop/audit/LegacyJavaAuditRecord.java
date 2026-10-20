package com.course.interop.audit;

public class LegacyJavaAuditRecord {
    private final String eventId;
    private final String actorUsername;
    private final int severityCode;

    public LegacyJavaAuditRecord(String eventId, String actorUsername, int severityCode) {
        this.eventId = eventId;
        this.actorUsername = actorUsername;
        this.severityCode = severityCode;
    }

    public String getEventId() {
        return eventId;
    }

    public String getActorUsername() {
        return actorUsername;
    }

    public int getSeverityCode() {
        return severityCode;
    }
}
