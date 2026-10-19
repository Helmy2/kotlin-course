package com.course.core.pricing

object PricingEngine {

    /**
     * Calculates the transaction processing fee.
     *
     * TODO: Task 1 - Refactor this placeholder into an idiomatic when/if expression
     * - VIP: 0% fee + (5.0 if cross-border else 0.0)
     * - GOLD: 1% fee + (15.0 if cross-border else 0.0)
     * - STANDARD: 2.5% fee + (15.0 if cross-border else 0.0)
     * - Any other tier: throw IllegalArgumentException("Unsupported tier: $tier")
     */
    fun calculateTransactionFee(
        amount: Double,
        tier: String = "STANDARD",
        isCrossBorder: Boolean = false
    ): Double {
        // TODO: Implement logic using when expression on tier.uppercase()
        throw NotImplementedError("calculateTransactionFee is not yet implemented")
    }

    /**
     * Generates an exponential backoff retry schedule for rate-limited clients.
     *
     * TODO: Task 2 - Generate exponential delays using range progression 0..<maxAttempts
     * Formula: baseDelayMs * 2^attempt
     */
    fun generateRetrySchedule(
        maxAttempts: Int = 4,
        baseDelayMs: Long = 100L
    ): List<Long> {
        // TODO: Implement using 0..<maxAttempts
        throw NotImplementedError("generateRetrySchedule is not yet implemented")
    }

    /**
     * Simple sliding window rate limit evaluation.
     */
    fun isRateLimitExceeded(
        callerIp: String,
        currentWindowRequests: Int,
        limit: Int = 100
    ): Boolean {
        require(callerIp.isNotBlank()) { "Caller IP must not be blank" }
        return currentWindowRequests >= limit
    }
}
