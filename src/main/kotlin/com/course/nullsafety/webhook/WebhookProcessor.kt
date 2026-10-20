package com.course.nullsafety.webhook

data class WebhookPayload(
    val transactionId: String?,
    val customerEmail: String?,
    val currency: String?,
    val rawAmounts: List<String?>?
)

data class ValidatedBatch(
    val transactionId: String,
    val customerEmail: String?,
    val currency: String,
    val validAmounts: List<Double>,
    val totalAmount: Double
)

object WebhookProcessor {

    /**
     * Sanitizes an unreliable third-party webhook payload.
     *
     * TODO: Implement guard clauses and collection pipeline:
     * 1. Fail fast if payload or payload.transactionId is null/blank (throw IllegalArgumentException)
     * 2. Normalize currency (fallback to "EGP")
     * 3. Convert rawAmounts (handling null container, null items, non-numbers) into positive Double values
     * 4. Return ValidatedBatch
     */
    fun processWebhook(payload: WebhookPayload?): ValidatedBatch {
        throw NotImplementedError("processWebhook is not yet implemented")
    }
}
