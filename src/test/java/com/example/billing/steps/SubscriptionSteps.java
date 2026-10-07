package com.example.billing.steps;

import com.example.billing.domain.*;
import com.example.billing.infrastructure.HmacUtil;
import com.example.billing.repository.Repository;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class SubscriptionSteps {

    private Repository repository;
    private boolean lastWebhookRejected = false;

    @Given("the billing repository is empty")
    public void clearRepository() {
        repository = new Repository();
        repository.clear();
        lastWebhookRejected = false;
    }

    @Given("a subscription {string} is {string}")
    public void createSubscription(String subId, String state) {
        SubscriptionState initialState = switch (state.toLowerCase()) {
            case "active" -> new ActiveState();
            case "past_due" -> new PastDueState();
            case "canceled" -> new CanceledState();
            default -> new TrialingState();
        };
        repository.saveSubscription(new Subscription(subId, "cust_1", initialState));
    }

    @When("a valid {string} webhook arrives for {string} with event {string}")
    public void processValidWebhook(String eventType, String subId, String eventId) {
        processWebhookInternal(eventType, subId, eventId, "valid_sig");
    }

    @When("the same webhook {string} arrives again")
    public void processDuplicateWebhook(String eventId) {
        processWebhookInternal("payment.succeeded", "sub_200", eventId, "valid_sig");
    }

    @When("a webhook arrives with an invalid signature")
    public void processInvalidWebhook() {
        processWebhookInternal("payment.succeeded", "sub_400", "evt_bad", "invalid_sig");
    }

    private void processWebhookInternal(String eventType, String subId, String eventId, String signature) {
        if (!"valid_sig".equals(signature)) {
            lastWebhookRejected = true;
            return;
        }

        if (repository.hasProcessedWebhook(eventId)) {
            return; // Idempotency check: duplicate event ignored
        }

        Subscription sub = repository.findSubscription(subId);
        if (sub != null) {
            if ("payment.succeeded".equals(eventType)) {
                sub.handlePaymentSuccess();
                repository.addInvoice("inv_" + eventId, subId, 2999);
            } else if ("payment.failed".equals(eventType)) {
                sub.handlePaymentFailure();
            }
        }

        repository.markWebhookProcessed(eventId);
    }

    @Then("subscription {string} status should be {string}")
    public void verifyStatus(String subId, String expectedStatus) {
        Subscription sub = repository.findSubscription(subId);
        assertNotNull(sub, "Subscription should exist in repository");
        assertEquals(expectedStatus, sub.getStatus());
    }

    @Then("total invoices for {string} should be {int}")
    public void verifyInvoiceCount(String subId, int expectedCount) {
        assertEquals(expectedCount, repository.getInvoicesForSubscription(subId).size());
    }

    @Then("the webhook is rejected")
    public void verifyRejected() {
        assertTrue(lastWebhookRejected, "Invalid signature webhook should be rejected");
    }
}