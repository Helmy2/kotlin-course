# Solution Explanation: Concept 03 - In-Memory Wallet Ledger

## Architectural Decisions & Kotlin Idioms

### 1. Backing Properties for Encapsulated Collections
In Java, returning a reference to an internal `List<String>` allows external callers to clear or modify the list directly (`wallet.getHistory().clear()`), violating domain encapsulation.

In idiomatic Kotlin, we use the backing property pattern:
```kotlin
private val _history = mutableListOf<String>()
val history: List<String>
    get() = _history.toList()
```
- `_history` is private and mutable.
- Public `history` exposes a read-only `List<String>` and returns a defensive snapshot via `_history.toList()`.

### 2. Read-Only State with `private set`
```kotlin
var balance: Double = 0.0
    private set
```
External classes can inspect `wallet.balance`, but any mutation is only possible through audited domain methods (`credit`, `debit`).

### 3. Functional Interface (`fun interface`) Strategy
`TransactionValidator` is declared as a `fun interface`, allowing SAM (Single Abstract Method) conversion so callers can pass lambdas directly:
```kotlin
val validator = TransactionValidator { amount -> amount <= 1000.0 }
```
