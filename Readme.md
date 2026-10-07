# Subscription & Billing Service - BDD Test Automation Suite

An automated BDD test suite and hybrid service fixture for a Java 17 Subscription & Billing Service built using **Cucumber**, **JUnit 5**, and **Maven**.

---

## 🛠️ Prerequisites & Tech Stack

* **Language:** Java 17
* **Build Tool:** Apache Maven 3.8+
* **Frameworks:** Cucumber (BDD), JUnit 5
* **Design Patterns:** State, Builder, Strategy, Repository

---

## 🚀 How to Run the Tests

To execute the full BDD test suite locally, run:

`mvn clean test`

### 📊 Test Reports
After running the tests, view execution results at:
* `target/cucumber-reports.html`

---

## 🏗️ Architecture Summary
* **BDD Specs:** Plain-text feature files defining subscription lifecycles, HMAC verification, and webhook idempotency.
* **Design Patterns:** Uses the **State Pattern** to enforce valid transitions, **Builder** for dynamic payloads, **Strategy** for mock payment gateways, and **Repository** for database event tracking.