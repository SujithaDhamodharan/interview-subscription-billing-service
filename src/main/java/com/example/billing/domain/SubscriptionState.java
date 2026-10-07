package com.example.billing.domain;

public interface SubscriptionState {
    String getName();
    void handlePaymentSuccess(Subscription sub);
    void handlePaymentFailure(Subscription sub);
    void cancel(Subscription sub);
}