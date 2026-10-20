package com.course.coroutines.kyc

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import kotlin.system.measureTimeMillis

data class KycResult(
    val passed: Boolean,
    val executionTimeMs: Long,
    val details: Map<String, Boolean>
)

object KycVerificationService {

    suspend fun checkNationalId(id: String): Boolean {
        delay(300)
        return id.startsWith("2") || id.startsWith("3")
    }

    suspend fun checkSanctionsList(id: String): Boolean {
        delay(400)
        return !id.endsWith("99")
    }

    suspend fun checkCreditScore(id: String): Boolean {
        delay(350)
        return id.length >= 6
    }

    /**
     * Executes the 3 KYC checks serially.
     */
    suspend fun verifyKycSequential(id: String): KycResult {
        val details = mutableMapOf<String, Boolean>()
        val elapsed = measureTimeMillis {
            details["nationalId"] = checkNationalId(id)
            details["sanctions"] = checkSanctionsList(id)
            details["creditScore"] = checkCreditScore(id)
        }

        val allPassed = details.values.all { it }
        return KycResult(passed = allPassed, executionTimeMs = elapsed, details = details)
    }

    /**
     * Executes the 3 KYC checks concurrently using async/await inside coroutineScope.
     */
    suspend fun verifyKycConcurrent(id: String): KycResult = coroutineScope {
        val details = mutableMapOf<String, Boolean>()
        val elapsed = measureTimeMillis {
            val nationalIdDeferred = async { checkNationalId(id) }
            val sanctionsDeferred = async { checkSanctionsList(id) }
            val creditScoreDeferred = async { checkCreditScore(id) }

            details["nationalId"] = nationalIdDeferred.await()
            details["sanctions"] = sanctionsDeferred.await()
            details["creditScore"] = creditScoreDeferred.await()
        }

        val allPassed = details.values.all { it }
        KycResult(passed = allPassed, executionTimeMs = elapsed, details = details)
    }

    /**
     * Executes concurrent KYC verification bounded by a timeout.
     */
    suspend fun verifyKycWithTimeout(id: String, timeoutMs: Long): KycResult {
        return withTimeout(timeoutMs) {
            verifyKycConcurrent(id)
        }
    }
}
