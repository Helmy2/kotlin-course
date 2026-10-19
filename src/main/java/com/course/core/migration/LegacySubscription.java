package com.course.core.migration;

public class LegacySubscription {
    private final String id;
    private final String customerId;
    private String planCode;
    private int maxSeats;
    private boolean cancelled;

    public LegacySubscription(String id, String customerId) {
        this(id, customerId, "FREE", 5);
    }

    public LegacySubscription(String id, String customerId, String planCode, int maxSeats) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id required");
        if (customerId == null || customerId.isBlank()) throw new IllegalArgumentException("customerId required");
        if (maxSeats < 1) throw new IllegalArgumentException("maxSeats must be >= 1");
        this.id = id;
        this.customerId = customerId;
        this.planCode = planCode.toUpperCase();
        this.maxSeats = maxSeats;
        this.cancelled = false;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getPlanCode() { return planCode; }
    public int getMaxSeats() { return maxSeats; }
    public boolean isCancelled() { return cancelled; }

    public void upgrade(String newPlanCode, int newMaxSeats) {
        if (cancelled) throw new IllegalStateException("Cannot upgrade cancelled subscription");
        if (newMaxSeats < this.maxSeats) throw new IllegalArgumentException("Downgrade not allowed via upgrade()");
        this.planCode = newPlanCode.toUpperCase();
        this.maxSeats = newMaxSeats;
    }

    public void cancel() {
        this.cancelled = true;
    }
}
