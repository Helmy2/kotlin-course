# Complete Branch Catalog & Learning Objectives

This catalog provides an interactive reference for all 18 starter and solution branches across the 9 hands-on concepts in the **Kotlin for Backend Engineering Master Course**.

---

## Day 1: Core Kotlin Concepts

### Concept 01: Backend Rate-Limiter & Fee Engine
- **Starter Branch:** `01-rate-limiter-fee-engine-starter`
- **Solution Branch:** `01-rate-limiter-fee-engine-solution`
- **Focus:** Expressions over statements, pattern matching with `when`, default arguments, named parameters, range loops (`0..<maxAttempts`), and exponential backoff calculations.
- **Key Files:** `com.course.core.pricing.PricingEngine.kt`
- **Tests:** `com.course.core.pricing.PricingEngineTest`
- **Command:** `./gradlew test --tests "com.course.core.pricing.*"`

### Concept 02: Migrating Legacy Java Domain
- **Starter Branch:** `02-java-subscription-migration-starter`
- **Solution Branch:** `02-java-subscription-migration-solution`
- **Focus:** Strangler fig migration strategy, refactoring verbose Java JavaBeans (55+ lines) into idiomatic Kotlin classes (<25 lines), primary constructors with property declarations, `init` block invariant checks, and default parameters eliminating constructor telescoping.
- **Key Files:** `LegacySubscription.java` → `Subscription.kt`
- **Tests:** `com.course.core.migration.SubscriptionMigrationTest`
- **Command:** `./gradlew test --tests "com.course.core.migration.*"`

### Concept 03: In-Memory Wallet Ledger Engine
- **Starter Branch:** `03-wallet-ledger-engine-starter`
- **Solution Branch:** `03-wallet-ledger-engine-solution`
- **Focus:** Class design, encapsulation with `private set`, backing properties (`_history` vs public read-only `history`), interface implementation (`TransactionValidator`), custom getters, and state mutability controls.
- **Key Files:** `com.course.core.wallet.Wallet.kt`, `TransactionValidator.kt`
- **Tests:** `com.course.core.wallet.WalletLedgerTest`
- **Command:** `./gradlew test --tests "com.course.core.wallet.*"`

---

## Day 2: Null Safety & Advanced Features

### Concept 04: Sanitizing Unreliable Webhook Payloads
- **Starter Branch:** `04-null-safe-webhook-processor-starter`
- **Solution Branch:** `04-null-safe-webhook-processor-solution`
- **Focus:** Compile-time null safety, guard clauses with Elvis operator (`?:`), safe calls (`?.`), distinguishing missing elements from missing containers, `orEmpty()`, `mapNotNull`, and functional data sanitization pipelines with `toDoubleOrNull()`.
- **Key Files:** `com.course.nullsafety.webhook.WebhookProcessor.kt`
- **Tests:** `com.course.nullsafety.webhook.WebhookProcessorTest`
- **Command:** `./gradlew test --tests "com.course.nullsafety.webhook.*"`

### Concept 05: Rich Domain Modeling with Data Classes
- **Starter Branch:** `05-rich-domain-modeling-starter`
- **Solution Branch:** `05-rich-domain-modeling-solution`
- **Focus:** First-class value objects, `data class` rules and copy semantics (`.copy()`), private primary constructors, companion object static factories (`Money.of()`, `Money.zero()`), rounding rules, and immutable domain state transitions.
- **Key Files:** `com.course.nullsafety.domain.Money.kt`, `OrderSnapshot.kt`
- **Tests:** `com.course.nullsafety.domain.RichDomainModelTest`
- **Command:** `./gradlew test --tests "com.course.nullsafety.domain.*"`

### Concept 06: Null-Safe DTO-to-Domain Extension Mapping
- **Starter Branch:** `06-dto-extension-mapping-starter`
- **Solution Branch:** `06-dto-extension-mapping-solution`
- **Focus:** Extension functions and properties, anti-corruption layers, handling nullable receivers (`RawOrderPayload?.toDomainOrder()`), trimming and sanitizing input without MapStruct/reflection, and returning Kotlin `Result<T>`.
- **Key Files:** `com.course.nullsafety.mapping.OrderMappingExtensions.kt`
- **Tests:** `com.course.nullsafety.mapping.OrderMappingExtensionsTest`
- **Command:** `./gradlew test --tests "com.course.nullsafety.mapping.*"`

---

## Day 3: JVM Interoperability & Coroutines

### Concept 07: Two-Way Java ↔ Kotlin Interop
- **Starter Branch:** `07-jvm-polyglot-interop-starter`
- **Solution Branch:** `07-jvm-polyglot-interop-solution`
- **Focus:** Polyglot JVM integration, `@JvmStatic` in companion objects, `@JvmOverloads` for constructor/method telescoping in Java, `@JvmField` for constants, `@Throws` for checked exception handling, and handling Java platform types (`T!`) safely.
- **Key Files:** `AuditService.kt`, `LegacyJavaAuditRecord.java`
- **Tests:** `com.course.interop.audit.PolyglotAuditTest`
- **Command:** `./gradlew test --tests "com.course.interop.audit.*"`

### Concept 08: Structured Concurrency & KYC Timeouts
- **Starter Branch:** `08-concurrent-kyc-engine-starter`
- **Solution Branch:** `08-concurrent-kyc-engine-solution`
- **Focus:** Suspending functions, concurrent decomposition with `async`/`await`, `coroutineScope`, benchmark sequential vs concurrent execution, and enforcing hard SLA boundaries with `withTimeout`.
- **Key Files:** `com.course.coroutines.kyc.KycVerificationService.kt`
- **Tests:** `com.course.coroutines.kyc.KycVerificationServiceTest`
- **Command:** `./gradlew test --tests "com.course.coroutines.kyc.*"`

### Concept 09: Resilient Microservice Checkout Aggregator (Capstone)
- **Starter Branch:** `09-coroutine-checkout-aggregator-starter`
- **Solution Branch:** `09-coroutine-checkout-aggregator-solution`
- **Focus:** Capstone structured concurrency, parallel downstream service calls, resilient partial failure isolation with `supervisorScope`, graceful degradation with fallbacks, and Spring Boot non-blocking controller integration.
- **Key Files:** `com.course.coroutines.checkout.OrderSummaryAggregator.kt`
- **Tests:** `com.course.coroutines.checkout.OrderSummaryAggregatorTest`
- **Command:** `./gradlew test --tests "com.course.coroutines.checkout.*"`
