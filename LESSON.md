# Lesson 09: Capstone - Resilient Microservice Checkout Aggregator

> "In distributed systems, partial failure is inevitable. Resilient architectures isolate downstream failure domains using supervisorScope and graceful fallbacks."

---

## 🎯 Learning Objectives
1. **Partial Failure Isolation with `supervisorScope`**: Prevent secondary service failures from cancelling the primary transaction.
2. **Graceful Degradation**: Fall back to safe defaults (0 loyalty points, flat shipping rate) and collect diagnostic warnings without throwing 500 errors to the customer.
3. **Critical vs Non-Critical Service Boundaries**: Differentiate between critical failures that must abort execution and optional enrichments that can degrade gracefully.
4. **Spring Boot Readiness**: Design non-blocking `suspend` aggregation workflows compatible with Spring WebFlux and coroutine controllers.

---

## 🏗️ Scenario: Checkout Screen Aggregator
A high-throughput e-commerce checkout aggregator gathers data from three downstream microservices in parallel:
1. **Core Order Service (Critical - 200ms)**: If this call fails, the transaction is invalid and the entire aggregation must fail immediately.
2. **Loyalty Service (Non-Critical - 250ms)**: Occasionally times out or throws `RuntimeException`. If it fails, fall back to `0` points and record a warning.
3. **Shipping Estimator (Non-Critical with Tight SLA - 150ms normal / 800ms slow)**: Wrapped in a `300ms` timeout. If it times out or fails, fall back to flat rate `50.0` and record a warning.

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/coroutines/checkout/OrderSummaryAggregator.kt`:
1. Implement downstream mock helpers:
   - `fetchOrderSubtotal(orderId: String)`: delay 200ms, returns 450.0 (or throws if orderId is blank).
   - `fetchLoyaltyPoints(userId: String, simulateFailure: Boolean)`: delay 250ms, returns 120 points (or throws `RuntimeException`).
   - `estimateShippingFee(orderId: String, slowNetwork: Boolean)`: delay 150ms (or 800ms if `slowNetwork`), returns 25.0.
2. Implement `suspend fun aggregateCheckout(...)`:
   - Enclose operations inside `supervisorScope`.
   - Launch all 3 calls in parallel using `async`.
   - Await critical order subtotal (allowing exceptions to propagate).
   - Await non-critical loyalty points inside a `try/catch` or `runCatching`, falling back to `0` and warning `"Loyalty service unavailable"`.
   - Wrap shipping call inside `withTimeoutOrNull(300)` with fallback to `50.0` and warning `"Shipping fee estimation timed out, applied default flat rate"`.
   - Return `CheckoutSummary`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.coroutines.checkout.OrderSummaryAggregatorTest"
```
