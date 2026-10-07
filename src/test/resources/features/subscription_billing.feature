Feature: Subscription Billing Operations

  Background:
    Given the billing repository is empty

  Scenario: Successful payment activates subscription
    Given a subscription "sub_100" is "trialing"
    When a valid "payment.succeeded" webhook arrives for "sub_100" with event "evt_001"
    Then subscription "sub_100" status should be "active"

  Scenario: Failed payment marks subscription as past due
    Given a subscription "sub_101" is "trialing"
    When a valid "payment.failed" webhook arrives for "sub_101" with event "evt_002"
    Then subscription "sub_101" status should be "past_due"

  Scenario: Duplicate webhooks are ignored
    Given a subscription "sub_200" is "trialing"
    When a valid "payment.succeeded" webhook arrives for "sub_200" with event "evt_003"
    And the same webhook "evt_003" arrives again
    Then subscription "sub_200" status should be "active"
    And total invoices for "sub_200" should be 1

  Scenario: Canceled subscription stays canceled after late payment
    Given a subscription "sub_300" is "canceled"
    When a valid "payment.succeeded" webhook arrives for "sub_300" with event "evt_004"
    Then subscription "sub_300" status should be "canceled"

  Scenario: Invalid signature blocks processing
    Given a subscription "sub_400" is "trialing"
    When a webhook arrives with an invalid signature
    Then the webhook is rejected
    And subscription "sub_400" status should be "trialing"