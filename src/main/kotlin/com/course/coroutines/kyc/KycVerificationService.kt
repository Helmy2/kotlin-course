package com.course.coroutines.kyc

import kotlinx.coroutines.delay

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
     *
     * TODO: Task 1 - Call checkNationalId, checkSanctionsList, checkCreditScore sequentially
     */
    suspend fun verifyKycSequential(id: String): KycResult {
        throw NotImplementedError("verifyKycSequential is not yet implemented")
    }

    /**
     * Executes the 3 KYC checks concurrently using async/await inside coroutineScope.
     *
     * TODO: Task 2 - Launch checks concurrently with async, then await results
     */
    suspend fun verifyKycConcurrent(id: String): KycResult {
        throw NotImplementedError("verifyKycConcurrent is not yet implemented")
    }

    /**
     * Executes concurrent KYC verification bounded by a timeout.
     *
     * TODO: Task 3 - Wrap verifyKycConcurrent in withTimeout
     */
    suspend fun verifyKycWithTimeout(id: String, timeoutMs: Long): KycResult {
        throw NotImplementedError("verifyKycWithTimeout is not yet implemented")
    }
}
