package com.course.core.pricing

object PricingEngine {

    /**
     * Calculates the transaction processing fee using idiomatic when and if expressions.
     */
    fun calculateTransactionFee(
        amount: Double,
        tier: String = "STANDARD",
        isCrossBorder: Boolean = false
    ): Double {
        require(amount >= 0.0) { "Amount must not be negative: $amount" }

        val normalizedTier = tier.uppercase()
        val baseRate = when (normalizedTier) {
            "VIP" -> 0.0
            "GOLD" -> 0.01
            "STANDARD" -> 0.025
            else -> throw IllegalArgumentException("Unsupported tier: $tier")
        }

        val baseFee = amount * baseRate
        val crossBorderFee = if (isCrossBorder) {
            if (normalizedTier == "VIP") 5.0 else 15.0
        } else {
            0.0
        }

        return baseFee + crossBorderFee
    }

    /**
     * Generates an exponential backoff retry schedule for rate-limited clients.
     * Uses the modern open-ended range `0..<maxAttempts`.
     */
    fun generateRetrySchedule(
        maxAttempts: Int = 4,
        baseDelayMs: Long = 100L
    ): List<Long> {
        require(maxAttempts > 0) { "maxAttempts must be > 0" }
        require(baseDelayMs > 0L) { "baseDelayMs must be > 0" }

        return (0..<maxAttempts).map { attempt ->
            baseDelayMs * (1L shl attempt)
        }
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
