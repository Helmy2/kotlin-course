# Lesson 08: Structured Concurrency & KYC Timeouts

> "Coroutines bring lightweight cooperative multitasking to backend systems, turning serial I/O latency into concurrent throughput without callback hell."

---

## 🎯 Learning Objectives
1. **Suspending Functions**: Model non-blocking asynchronous operations with `suspend fun`.
2. **Concurrent Decomposition with `async` / `await`**: Deconstruct independent remote I/O calls into concurrent child jobs inside `coroutineScope`.
3. **Avoid the Sequential `async.await()` Trap**: Prevent serializing tasks by launching all `async` deferreds before awaiting their results.
4. **SLA Bounds with `withTimeout`**: Enforce strict deadlines on downstream external checks.

---

## 🏗️ Scenario: Know Your Customer (KYC) Verification
A payment onboarding pipeline performs three independent remote verification checks:
1. `checkNationalId(id)`: 300ms
2. `checkSanctionsList(id)`: 400ms
3. `checkCreditScore(id)`: 350ms

Executing them serially takes $\approx 1050\text{ms}$. By running them concurrently inside `coroutineScope`, the overall turnaround drops to $\approx 400\text{ms}$ ($\max(300, 400, 350)$).

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/coroutines/kyc/KycVerificationService.kt`:
1. Inspect the simulated check methods: `checkNationalId`, `checkSanctionsList`, `checkCreditScore`.
2. Implement `suspend fun verifyKycSequential(id: String): KycResult`:
   - Invoke checks in serial order, measuring elapsed time.
3. Implement `suspend fun verifyKycConcurrent(id: String): KycResult`:
   - Use `coroutineScope`: launch checks with `async { ... }` and await results together.
   - Measure execution time to prove concurrent speedup.
4. Implement `suspend fun verifyKycWithTimeout(id: String, timeoutMs: Long): KycResult`:
   - Wrap the concurrent verification inside `withTimeout(timeoutMs)`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.coroutines.kyc.KycVerificationServiceTest"
```
