# Solution Explanation: Concept 09 - Capstone Resilient Checkout Aggregator

## Architectural Decisions & Kotlin Idioms

### 1. Failure Domain Isolation with `supervisorScope`
If we used standard `coroutineScope`, an unhandled exception in `fetchLoyaltyPoints` would automatically cancel all siblings (including the critical order fetch) and rethrow the exception out of the scope.

By using `supervisorScope`:
- A failure in `loyaltyDeferred` does NOT cancel `orderDeferred` or `shippingDeferred`.
- Each child coroutine failure domain remains completely isolated.

### 2. SLA Bounding via `withTimeoutOrNull`
```kotlin
val shippingDeferred = async {
    withTimeoutOrNull(300L) {
        estimateShippingFee(orderId, simulateSlowShipping)
    }
}
```
If network congestion slows shipping estimation down to 800ms, `withTimeoutOrNull` halts the coroutine at 300ms and yields `null`. The aggregator catches the missing value and applies the fallback flat fee of `50.0`, ensuring total response time never breaches customer SLAs.

### 3. Graceful Degradation & Warning Telemetry
Rather than returning a blank failure or a 500 internal server error, the API returns a full `CheckoutSummary` populated with diagnostic warnings (`"Loyalty service unavailable"`). The front-end can render the screen smoothly and prompt the customer without impeding their purchase flow.
