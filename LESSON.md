# Lesson 01: Backend Rate-Limiter & Fee Engine

> "Control flow in idiomatic Kotlin favors expressions that yield values over statements that produce side effects."

---

## 🎯 Learning Objectives
1. **Expressions Over Statements**: Use `when` and `if` as value-yielding expressions to initialize `val` immutably.
2. **Default & Named Arguments**: Eliminate method overloading sprawl by providing default parameters.
3. **Range Loops & Progressions**: Use modern open-ended ranges (`0..<maxAttempts`) and functional transformations.
4. **Defensive Validation**: Validate inputs with descriptive exceptions early in execution.

---

## 🏗️ Scenario: FinTech Gateway Pricing Engine
You are engineering the pricing and rate-limiting utility for a payment gateway:
- Transaction fee calculation depends on customer tier (`VIP`, `GOLD`, `STANDARD`) and whether the payment crosses international borders.
- Rate-limiting schedules use exponential backoff calculations for retrying throttled requests.

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/core/pricing/PricingEngine.kt`:
1. Implement `calculateTransactionFee(amount: Double, tier: String = "STANDARD", isCrossBorder: Boolean = false): Double`:
   - Match `tier.uppercase()` using `when`:
     - `"VIP"` -> base rate `0.0`
     - `"GOLD"` -> base rate `0.01` (1%)
     - `"STANDARD"` -> base rate `0.025` (2.5%)
     - Any other tier -> throw `IllegalArgumentException("Unsupported tier: $tier")`
   - Compute `baseFee = amount * baseRate`.
   - If `isCrossBorder` is true:
     - VIP adds flat `5.0`.
     - Non-VIP adds flat `15.0`.
   - Return total fee rounded to 2 decimal places.
2. Implement `generateRetrySchedule(maxAttempts: Int = 4, baseDelayMs: Long = 100L): List<Long>`:
   - Use a range loop `0..<maxAttempts` to generate delays: `baseDelayMs * (1L shl attempt)` (e.g. `[100, 200, 400, 800]`).
3. Implement `isRateLimitExceeded(callerIp: String, currentWindowRequests: Int, limit: Int = 100): Boolean`:
   - Returns true if `currentWindowRequests >= limit`.

---

## 🧪 Verification
Run the test suite:
```bash
./gradlew test --tests "com.course.core.pricing.PricingEngineTest"
```
