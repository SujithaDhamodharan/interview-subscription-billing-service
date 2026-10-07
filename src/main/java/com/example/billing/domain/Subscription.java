package com.example.billing.domain;

public class Subscription {
    private final String id;
    private final String customerId;
    private SubscriptionState state;

    public Subscription(String id, String customerId) {
        this.id = id;
        this.customerId = customerId;
        this.state = new TrialingState(); // Default initial state
    }

    public Subscription(String id, String customerId, SubscriptionState initialState) {
        this.id = id;
        this.customerId = customerId;
        this.state = initialState;
    }

    public String getId() { return id; }
    public String getCustomerId() { return customerId; }
    public String getStatus() { return state.getName(); }

    public void setState(SubscriptionState state) {
        this.state = state;
    }

    // State transition delegates
    public void handlePaymentSuccess() {
        state.handlePaymentSuccess(this);
    }

    public void handlePaymentFailure() {
        state.handlePaymentFailure(this);
    }

    public void cancel() {
        state.cancel(this);
    }
}