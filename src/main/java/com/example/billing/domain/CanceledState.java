package com.example.billing.domain;

public class CanceledState implements SubscriptionState {
    @Override
    public String getName() { return "canceled"; }

    @Override
    public void handlePaymentSuccess(Subscription sub) {
        // Late webhooks on canceled sub are ignored
    }

    @Override
    public void handlePaymentFailure(Subscription sub) {
        // Remains canceled
    }

    @Override
    public void cancel(Subscription sub) {
        throw new IllegalStateException("Subscription is already canceled");
    }
}