# Solution Explanation: Concept 08 - Concurrent KYC Engine

## Architectural Decisions & Kotlin Idioms

### 1. Concurrent Decomposition with `async` and `coroutineScope`
```kotlin
suspend fun verifyKycConcurrent(id: String): KycResult = coroutineScope {
    val nationalIdDeferred = async { checkNationalId(id) }
    val sanctionsDeferred = async { checkSanctionsList(id) }
    val creditScoreDeferred = async { checkCreditScore(id) }

    val r1 = nationalIdDeferred.await()
    val r2 = sanctionsDeferred.await()
    val r3 = creditScoreDeferred.await()
}
```
Inside `coroutineScope`, launching three `async` builders begins all three simulated I/O tasks simultaneously on the scheduler. When `.await()` is called, the coroutine yields until all tasks finish. The overall duration is $\max(300, 400, 350) = 400\text{ms}$ instead of $1050\text{ms}$.

### 2. The Sequential `async.await()` Trap
Writing:
```kotlin
// ANTIPATTERN: Serializes execution!
val r1 = async { checkNationalId(id) }.await()
val r2 = async { checkSanctionsList(id) }.await()
```
immediately blocks progression on line 1, effectively eliminating all concurrency benefits. Always launch all deferred tasks before awaiting any results!

### 3. Hard SLA Enforcement with `withTimeout`
```kotlin
withTimeout(timeoutMs) {
    verifyKycConcurrent(id)
}
```
If the downstream checks take longer than `timeoutMs`, `withTimeout` cancels all child coroutines in the scope immediately and throws `TimeoutCancellationException`, guarding backend thread pools from connection exhaustion.
