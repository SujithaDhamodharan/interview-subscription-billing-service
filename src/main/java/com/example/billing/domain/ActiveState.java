package com.example.billing.domain;

public class ActiveState implements SubscriptionState {
    @Override
    public String getName() { return "active"; }

    @Override
    public void handlePaymentSuccess(Subscription sub) {
        // Already active
    }

    @Override
    public void handlePaymentFailure(Subscription sub) {
        sub.setState(new PastDueState());
    }

    @Override
    public void cancel(Subscription sub) {
        sub.setState(new CanceledState());
    }
}