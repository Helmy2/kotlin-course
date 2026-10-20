# Solution Explanation: Concept 04 - Null-Safe Webhook Processor

## Architectural Decisions & Kotlin Idioms

### 1. Guard Clause Smart-Casting
```kotlin
val safePayload = payload ?: throw IllegalArgumentException("Webhook payload must not be null")
```
Once this guard clause executes, the Kotlin compiler smart-casts `payload` to non-null `WebhookPayload` for all remaining lines in the function body.

### 2. Combining `takeIf` with Elvis Operator
```kotlin
val txId = safePayload.transactionId?.takeIf { it.isNotBlank() }
    ?: throw IllegalArgumentException("Missing or blank transaction ID")
```
`takeIf` turns whitespace-only strings (like `"   "`) into `null`, allowing the Elvis operator (`?:`) to catch both missing `null`s and blank strings cleanly in a single check.

### 3. Collection Pipeline: `orEmpty()`, `mapNotNull`, and `filter`
```kotlin
val validAmounts = safePayload.rawAmounts
    .orEmpty()
    .mapNotNull { it?.trim()?.toDoubleOrNull() }
    .filter { it > 0.0 }
```
- `orEmpty()` safely replaces `null` list containers with `emptyList()`.
- `toDoubleOrNull()` safely converts valid string numbers to `Double` and returns `null` for non-numeric input (like `"corrupt"`), eliminating `NumberFormatException`.
- `mapNotNull` discards every `null` entry automatically.
- `filter { it > 0.0 }` strips zero and negative amounts.
