# Lesson 06: Null-Safe DTO-to-Domain Mapping with Extension Functions

> "Extension functions provide an idiomatic anti-corruption layer, decoupling domain entities from external JSON/DTO schemas without reflection."

---

## 🎯 Learning Objectives
1. **Nullable Receiver Extensions**: Declare extension functions on nullable types (`RawOrderPayload?.toDomainOrder()`) to encapsulate receiver null-checks inside the method.
2. **Domain Mapping Without Reflection**: Replace heavy mapping frameworks (like MapStruct or ModelMapper) with clean, zero-overhead extension functions.
3. **Idiomatic Error Modeling with `Result<T>`**: Return functional `Result.success` and `Result.failure` instead of throwing unhandled exceptions.
4. **Anti-Corruption Layer Pattern**: Prevent malformed external schema fields from polluting internal domain invariants.

---

## 🏗️ Scenario: Anti-Corruption REST Boundary
External partners submit unverified JSON payloads represented by `RawOrderPayload`:
- All fields are nullable (`rawId: String?`, `lineItems: List<String?>?`, `rawAmount: Double?`, `rawCurrency: String?`).
- Your extension function must validate and map this payload into the strict `OrderSnapshot` domain model from Lesson 05, returning `Result<OrderSnapshot>`.

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/nullsafety/mapping/OrderMappingExtensions.kt`:
1. Define `data class RawOrderPayload(...)`.
2. Implement `fun RawOrderPayload?.toDomainOrder(): Result<OrderSnapshot>`:
   - If receiver is `null`, return `Result.failure(IllegalArgumentException("Payload must not be null"))`.
   - Validate `rawId` (must not be blank); else return `Result.failure`.
   - Sanitize `lineItems`: filter out nulls and blanks. If the list is empty, return `Result.failure`.
   - Validate `rawAmount` (must not be null and >= 0.0); else return `Result.failure`.
   - Default `rawCurrency` to `Money.DEFAULT_CURRENCY` if null or blank.
   - Construct `Money.of(rawAmount, resolvedCurrency)` and `OrderSnapshot(validId, validItems, money, "CREATED")`.
   - Return `Result.success(order)`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.nullsafety.mapping.OrderMappingExtensionsTest"
```
