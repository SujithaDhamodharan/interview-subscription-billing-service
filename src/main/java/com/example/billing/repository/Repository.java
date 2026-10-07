package com.example.billing.repository;

import com.example.billing.domain.Subscription;
import java.util.*;

public class Repository {
    private final Map<String, Subscription> subscriptions = new HashMap<>();
    private final Set<String> processedWebhooks = new HashSet<>();
    private final List<Map<String, Object>> invoices = new ArrayList<>();

    // Subscription operations
    public void saveSubscription(Subscription sub) {
        subscriptions.put(sub.getId(), sub);
    }

    public Subscription findSubscription(String id) {
        return subscriptions.get(id);
    }

    // Webhook Idempotency tracking
    public boolean hasProcessedWebhook(String eventId) {
        return processedWebhooks.contains(eventId);
    }

    public void markWebhookProcessed(String eventId) {
        processedWebhooks.add(eventId);
    }

    // Invoice ledger
    public void addInvoice(String invoiceId, String subId, int amount) {
        Map<String, Object> invoice = new HashMap<>();
        invoice.put("id", invoiceId);
        invoice.put("subId", subId);
        invoice.put("amount", amount);
        invoices.add(invoice);
    }

    public List<Map<String, Object>> getInvoicesForSubscription(String subId) {
        return invoices.stream()
                .filter(i -> subId.equals(i.get("subId")))
                .toList();
    }

    public void clear() {
        subscriptions.clear();
        processedWebhooks.clear();
        invoices.clear();
    }
}