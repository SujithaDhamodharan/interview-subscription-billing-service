# Subscription & Billing Service — SDET Test Strategy & Approach

## 1. Executive Summary & System Understanding
The System Under Test (SUT) is a stateful backend service managing subscription lifecycles (`trialing`, `active`, `past_due`, `canceled`). Lifecycles are driven by API calls and asynchronous, signed webhooks from an external payment provider.

This automation solution proves system correctness across:
- **API Contracts**: Payload validation, endpoint status handling, and HMAC signature security.
- **State Machine Rules**: Enforcing valid state transitions while structurally rejecting illegal transitions.
- **Persistence & Auditability**: Ensuring database records (`subscriptions`, `invoices`, `webhook_events`) match API-visible states without duplicate side effects.
- **Integration Reliability**: Mocking payment provider behavior (success, decline, timeouts) and asserting idempotency during duplicate/out-of-order webhook delivery.

---

## 2. Object-Oriented Framework & Design Patterns

1. **Behavior-Driven Development (Cucumber / Gherkin)**: Expresses business requirements as readable `.feature` specifications executing via Java step definitions.
2. **State Pattern (`SubscriptionState`)**: Encapsulates transition logic into concrete state classes (`TrialingState`, `ActiveState`, `PastDueState`, `CanceledState`), preventing improper `status` string mutations.
3. **Strategy Pattern (`PlanStrategy`)**: Abstracts tier-specific pricing and trial rules (`BasicPlan`, `ProPlan`).
4. **Builder Pattern (`WebhookBuilder`)**: Fluent creation of valid/malformed test data payloads and calculation of HMAC signatures.
5. **Repository Pattern (`Repository`)**: Encapsulates persistence logic and exposes explicit assertion APIs to confirm data consistency.

---

## 3. Coverage & Invariant Verification Matrix

| Category | Invariant Checked | Verification Layer |
| :--- | :--- | :--- |
| **Idempotency** | Duplicate webhook `event_id` is processed exactly once; only 1 invoice created | DB & API Response |
| **State Security** | Late `payment.succeeded` webhook on `canceled` subscription is ignored | DB & State Machine |
| **Mocking** | Payment provider charge called only for actions requiring immediate billing | Mock Interaction |
| **Consistency** | Subscription state in DB matches active invoice count at all times | Persistence Layer |

---

## 4. Implementation Checklist
- [x] Test Strategy Document & Architecture Design
- [ ] Core Domain Logic & State Machine (`src/main/java`)
- [ ] Mock Payment Provider & Repository Layer
- [ ] BDD Feature Files & Step Definitions (`src/test/java`)
- [ ] End-to-End Test Execution & Validation
- *PR initialized for phase review.*