# Solution Explanation: Concept 02 - Java Subscription Migration

## Architectural Decisions & Kotlin Idioms

### 1. 55 Lines of Java Reduced to 23 Lines of Kotlin
The legacy Java implementation required:
- 2 telescoping constructor overloads
- 5 getter methods
- Manual field assignment boilerplate
- Verbose null checks with `if (id == null || id.isBlank())`

In Kotlin, the primary constructor declares read-only properties (`val id`, `val customerId`) in the class header itself.

### 2. Preconditions and Invariants with `require` and `check`
Kotlin standard library provides semantic preconditions:
- `require(boolean)` throws `IllegalArgumentException` (used for invalid client arguments).
- `check(boolean)` throws `IllegalStateException` (used when the current object lifecycle state forbids the action).

### 3. Encapsulation via `private set`
```kotlin
var planCode: String = planCode.uppercase()
    private set
```
External consumers can freely read `sub.planCode`, but only methods inside `Subscription` can mutate it. This prevents callers from bypassing the invariant validation in `upgrade()`.
