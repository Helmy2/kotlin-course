# Lesson 05: Rich Domain Modeling with Data Classes & Companion Factories

> "Value objects should encapsulate their construction rules and invariants using companion object factories and immutable data classes."

---

## 🎯 Learning Objectives
1. **Private Primary Constructors**: Restrict direct instantiation to guarantee all instances are validated and normalized.
2. **Companion Object Factories**: Replace secondary constructors with expressive static factories (`Money.of()`, `Money.zero()`).
3. **Immutability & State Transitions with `copy()`**: Transition domain entities into new states without mutating existing objects.
4. **Operator Overloading**: Enable natural syntax like `moneyA + moneyB` with currency guard rails.

---

## 🏗️ Scenario: E-Commerce Value Objects
You are engineering the core domain model for an e-commerce checkout service:
- Monetary amounts must maintain exact 2-decimal scale (`RoundingMode.HALF_UP`) and never allow negative numbers.
- Discount applications create a new `OrderSnapshot` with `status = "DISCOUNTED"` while leaving the original snapshot completely unchanged.

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/nullsafety/domain/`:
1. Implement `Money.kt`:
   - `data class Money private constructor(val amount: BigDecimal, val currency: String)`
   - Companion object:
     - Constant `DEFAULT_CURRENCY = "EGP"`
     - `of(amount: Double, currency: String = DEFAULT_CURRENCY): Money`: validates non-negative amount, non-blank currency, scales to 2 decimals (`RoundingMode.HALF_UP`), normalizes currency uppercase.
     - `zero(currency: String = DEFAULT_CURRENCY): Money`
   - Implement `operator fun plus(other: Money): Money`: validates matching currencies.
2. Implement `OrderSnapshot.kt`:
   - `data class OrderSnapshot(val orderId: String, val items: List<String>, val total: Money, val status: String)`
   - Implement `applyDiscount(discountPercent: Int): OrderSnapshot`:
     - Validates `discountPercent in 1..100`.
     - Calculates discounted total via `Money.of(...)`.
     - Returns `this.copy(total = newTotal, status = "DISCOUNTED")`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.nullsafety.domain.RichDomainModelTest"
```
