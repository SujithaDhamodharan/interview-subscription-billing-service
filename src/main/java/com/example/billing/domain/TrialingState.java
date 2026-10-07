package com.example.billing.domain;

public class TrialingState implements SubscriptionState {
    @Override
    public String getName() { return "trialing"; }

    @Override
    public void handlePaymentSuccess(Subscription sub) {
        sub.setState(new ActiveState());
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