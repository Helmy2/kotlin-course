# Solution Explanation: Concept 01 - Rate-Limiter & Fee Engine

## Architectural Decisions & Kotlin Idioms

### 1. Expressions Over Statements
In Java, determining variables conditionally often involves mutable `double fee = 0.0;` declarations followed by multiple mutating `if/else` ladders. In idiomatic Kotlin:
```kotlin
val baseRate = when (normalizedTier) {
    "VIP" -> 0.0
    "GOLD" -> 0.01
    "STANDARD" -> 0.025
    else -> throw IllegalArgumentException("Unsupported tier: $tier")
}
```
The `when` block acts directly as an expression yielding a value. The compiler enforces exhaustiveness, and `baseRate` remains strictly read-only (`val`).

### 2. Nested Expression Logic for Cross-Border Surcharges
Rather than branching across entire calculation blocks, the surcharge is computed in a localized expression:
```kotlin
val crossBorderFee = if (isCrossBorder) {
    if (normalizedTier == "VIP") 5.0 else 15.0
} else {
    0.0
}
```

### 3. Open-Ended Ranges & Functional Collections
Using `(0..<maxAttempts).map { ... }` provides:
- Clean off-by-one prevention (equivalent to `attempt = 0; attempt < maxAttempts; attempt++` in C/Java).
- Functional transformation directly returning an immutable `List<Long>`.
- Bitshift operator `(1L shl attempt)` computing powers of 2 without floating-point math overhead (`Math.pow`).
