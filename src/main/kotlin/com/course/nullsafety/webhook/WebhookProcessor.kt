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

    fun processWebhook(payload: WebhookPayload?): ValidatedBatch {
        val safePayload = payload ?: throw IllegalArgumentException("Webhook payload must not be null")

        val txId = safePayload.transactionId?.takeIf { it.isNotBlank() }
            ?: throw IllegalArgumentException("Missing or blank transaction ID")

        val currency = safePayload.currency
            ?.trim()
            ?.uppercase()
            ?.takeIf { it.isNotBlank() }
            ?: "EGP"

        val validAmounts = safePayload.rawAmounts
            .orEmpty()
            .mapNotNull { it?.trim()?.toDoubleOrNull() }
            .filter { it > 0.0 }

        val total = validAmounts.sum()

        return ValidatedBatch(
            transactionId = txId,
            customerEmail = safePayload.customerEmail?.trim()?.takeIf { it.isNotBlank() },
            currency = currency,
            validAmounts = validAmounts,
            totalAmount = total
        )
    }
}
