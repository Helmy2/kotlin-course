# Solution Explanation: Concept 05 - Rich Domain Modeling

## Architectural Decisions & Kotlin Idioms

### 1. Private Primary Constructor on Data Classes
```kotlin
data class Money private constructor(
    val amount: BigDecimal,
    val currency: String
)
```
By making the primary constructor private, callers cannot instantiate `Money` with raw unrounded `BigDecimal`s or lowercase invalid currency codes. Construction is exclusively routed through the companion factory `Money.of(...)`.

### 2. Companion Object Static Factories
```kotlin
companion object {
    fun of(amount: Double, currency: String = DEFAULT_CURRENCY): Money { ... }
    fun zero(currency: String = DEFAULT_CURRENCY): Money = of(0.0, currency)
}
```
Unlike constructors, companion factories:
- Can have descriptive names (`zero`, `of`).
- Can enforce rounding rules (`RoundingMode.HALF_UP`).
- Can return cached instances (e.g. caching `ZERO_EGP`).

### 3. State Transitions via `.copy()`
```kotlin
return this.copy(
    total = newTotal,
    status = "DISCOUNTED"
)
```
In Clean Architecture and Domain-Driven Design (DDD), immutable entities never mutate their fields in place. Instead, `.copy(...)` produces an updated immutable snapshot, avoiding multithreaded race conditions.
