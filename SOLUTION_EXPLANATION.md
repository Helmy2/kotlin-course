# Solution Explanation: Concept 06 - DTO Extension Mapping

## Architectural Decisions & Kotlin Idioms

### 1. Nullable Receiver Extension Functions
```kotlin
fun RawOrderPayload?.toDomainOrder(): Result<OrderSnapshot>
```
Declaring the receiver as nullable (`RawOrderPayload?`) allows callers to write:
```kotlin
val result = nullablePayload.toDomainOrder()
```
Callers do not need defensive checks like `if (payload != null) payload.toDomainOrder() else ...`. All null handling is encapsulated directly inside the extension method.

### 2. Functional Error Handling with `Result<T>`
Instead of throwing checked/unchecked exceptions which can bypass caller control flow, the method returns Kotlin's built-in `Result<OrderSnapshot>`. Callers can leverage:
- `.getOrThrow()`
- `.getOrElse { fallback }`
- `.onSuccess { ... }.onFailure { ... }`

### 3. Anti-Corruption Layer (ACL)
By keeping `RawOrderPayload` separate from the domain entity `OrderSnapshot` and sanitizing strings/lists during the mapping pass, malformed partner inputs cannot corrupt domain invariants.
