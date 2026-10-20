# Lesson 03: Building an In-Memory Wallet Ledger Engine

> "Encapsulation in Kotlin is achieved cleanly using backing properties and visibility modifiers without getter/setter ceremonies."

---

## 🎯 Learning Objectives
1. **Backing Properties Pattern**: Use `private val _history = mutableListOf<String>()` combined with a public read-only computed property `val history: List<String> get() = _history.toList()` to protect collection state from external modification.
2. **Read-Only Property Mutation**: Restrict balance modification to internal methods using `var balance: Double = 0.0; private set`.
3. **Constructor Invariant Enforcement**: Enforce supported currency codes and positive transaction bounds.
4. **Interface Contracts & Strategy Injection**: Inject optional validator policies into domain entities.

---

## 🏗️ Scenario: FinTech Wallet Ledger
You are implementing the core financial wallet for a payment system:
- Each wallet has an immutable ID, owner name, currency (`EGP`, `USD`, `EUR`), and dynamic balance.
- Balance can only be altered through audited `credit` and `debit` operations.
- History log must never be directly mutable from outside the wallet.

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/core/wallet/`:
1. Inspect `TransactionValidator.kt`.
2. Implement `Wallet.kt`:
   - Primary constructor: `val walletId: String`, `val ownerName: String`, `currency: String = "EGP"`, `private val validator: TransactionValidator? = null`.
   - Invariants in `init`:
     - `walletId` and `ownerName` must not be blank.
     - `currency` normalized to uppercase and must be in `{"EGP", "USD", "EUR"}`.
   - Encapsulation:
     - `var balance: Double = 0.0; private set`
     - `private val _history = mutableListOf<String>()`
     - `val history: List<String> get() = _history.toList()`
   - Methods:
     - `credit(amount: Double, note: String = "Deposit")`: updates balance, appends formatted entry to `_history`.
     - `debit(amount: Double, note: String = "Withdrawal")`: validates sufficient funds, updates balance, appends formatted entry to `_history`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.core.wallet.WalletLedgerTest"
```
