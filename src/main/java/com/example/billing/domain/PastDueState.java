package com.example.billing.domain;

public class PastDueState implements SubscriptionState {
    @Override
    public String getName() { return "past_due"; }

    @Override
    public void handlePaymentSuccess(Subscription sub) {
        sub.setState(new ActiveState());
    }

    @Override
    public void handlePaymentFailure(Subscription sub) {
        sub.setState(new CanceledState());
    }

    @Override
    public void cancel(Subscription sub) {
        sub.setState(new CanceledState());
    }
}