# Lesson 04: Sanitizing Unreliable External Webhook Payloads

> "The billion-dollar mistake of NullPointerExceptions is resolved at compile time through Kotlin's null-tracking type system."

---

## 🎯 Learning Objectives
1. **Elvis Operator (`?:`) for Guard Clauses**: Fail fast with `IllegalArgumentException` when critical identification fields are missing.
2. **Safe Calls (`?.`) with Fallbacks**: Normalize optional text fields safely using safe calls combined with fallback defaults.
3. **Nullable Collections**: Distinguish missing list containers (`List<T>?`) from lists containing null elements (`List<T?>`).
4. **Defensive Functional Pipelines**: Use `orEmpty()`, `mapNotNull`, and `toDoubleOrNull()` to eliminate all malformed and null inputs in a single expressive transformation chain.

---

## 🏗️ Scenario: Unreliable Payment Gateway Payloads
Third-party webhook providers deliver batch notifications where:
- The entire payload might be null.
- `transactionId` may be missing or blank.
- `rawAmounts` may be null, contain nulls, non-numeric strings (`"N/A"`), blanks, or negative refund values.
- `currency` might be unformatted or missing (default to `"EGP"`).

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/nullsafety/webhook/WebhookProcessor.kt`:
1. Implement `processWebhook(payload: WebhookPayload?): ValidatedBatch`:
   - Validate `payload?.transactionId` is non-null and not blank, failing fast with `IllegalArgumentException`.
   - Normalize `payload.currency` (trim, uppercase), falling back to `"EGP"` if missing or blank.
   - Process `payload.rawAmounts`:
     - Safely handle null container using `orEmpty()`.
     - Discard nulls, whitespace, and non-numeric entries using `mapNotNull { it?.trim()?.toDoubleOrNull() }`.
     - Filter strictly for positive amounts (`it > 0.0`).
   - Calculate total valid sum and return `ValidatedBatch`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.nullsafety.webhook.WebhookProcessorTest"
```
