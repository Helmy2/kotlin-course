# Lesson 02: Migrating a Legacy Java Domain Class

> "The Strangler Fig pattern allows teams to incrementally migrate Java codebases to Kotlin class by class without stopping feature delivery."

---

## 🎯 Learning Objectives
1. **Primary Constructors & Properties**: Declare fields directly in the class header, eliminating 30+ lines of Java getters and boilerplate assignments.
2. **Precondition Validation in `init` Blocks**: Enforce business invariants early before instances can escape construction.
3. **Encapsulation with `private set`**: Expose read-only property getters while restricting state mutations to explicit domain methods.
4. **Default Parameters Over Overloading**: Replace telescoping constructor ladders with clean default arguments.

---

## 🏗️ Scenario: The Strangler Fig Migration
In `src/main/java/com/course/core/migration/LegacySubscription.java`, an existing 55-line Java domain class manages subscription tiers and seats. Your goal is to migrate it into an idiomatic Kotlin class in `src/main/kotlin/com/course/core/migration/Subscription.kt` in **under 25 lines** while maintaining strict binary and behavioral parity.

---

## 📝 Refactoring Tasks
1. Inspect `LegacySubscription.java`.
2. Implement `src/main/kotlin/com/course/core/migration/Subscription.kt`:
   - Primary constructor: `val id: String`, `val customerId: String`, `planCode: String = "FREE"`, `maxSeats: Int = 5`.
   - Invariant validation in `init`:
     - `require(id.isNotBlank()) { "id required" }`
     - `require(customerId.isNotBlank()) { "customerId required" }`
     - `require(maxSeats >= 1) { "maxSeats must be >= 1" }`
   - Properties:
     - `var planCode: String = planCode.uppercase(); private set`
     - `var maxSeats: Int = maxSeats; private set`
     - `var isCancelled: Boolean = false; private set`
   - Domain methods:
     - `upgrade(newPlanCode: String, newMaxSeats: Int)`: check `!isCancelled` (else `IllegalStateException`) and `newMaxSeats >= maxSeats` (else `IllegalArgumentException`).
     - `cancel()`: sets `isCancelled = true`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.core.migration.SubscriptionMigrationTest"
```
