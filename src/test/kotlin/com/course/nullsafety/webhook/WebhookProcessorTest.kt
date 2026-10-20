package com.course.nullsafety.webhook

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WebhookProcessorTest {

    @Test
    @DisplayName("Null payload throws IllegalArgumentException")
    fun nullPayloadThrows() {
        assertThatThrownBy {
            WebhookProcessor.processWebhook(null)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Missing or blank transaction ID throws IllegalArgumentException")
    fun missingTxIdThrows() {
        val payloadWithNullId = WebhookPayload(null, "user@test.com", "USD", listOf("100.0"))
        assertThatThrownBy {
            WebhookProcessor.processWebhook(payloadWithNullId)
        }.isInstanceOf(IllegalArgumentException::class.java)

        val payloadWithBlankId = WebhookPayload("   ", "user@test.com", "USD", listOf("100.0"))
        assertThatThrownBy {
            WebhookProcessor.processWebhook(payloadWithBlankId)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Missing currency defaults to EGP")
    fun currencyDefaulting() {
        val payload = WebhookPayload(
            transactionId = "tx-1234",
            customerEmail = null,
            currency = null,
            rawAmounts = listOf("50.0")
        )
        val result = WebhookProcessor.processWebhook(payload)
        assertThat(result.currency).isEqualTo("EGP")
        assertThat(result.customerEmail).isNull()
    }

    @Test
    @DisplayName("Sanitizes messy list containing nulls, blanks, invalid strings, and negative values")
    fun batchSanitization() {
        val messyAmounts = listOf("10.5 ", null, "  ", "corrupt", "200.0", "-50.0", "0.0", "99.5")
        val payload = WebhookPayload(
            transactionId = "tx-9999",
            customerEmail = "test@company.com",
            currency = "usd",
            rawAmounts = messyAmounts
        )

        val result = WebhookProcessor.processWebhook(payload)
        assertThat(result.transactionId).isEqualTo("tx-9999")
        assertThat(result.currency).isEqualTo("USD")
        assertThat(result.validAmounts).containsExactly(10.5, 200.0, 99.5)
        assertThat(result.totalAmount).isEqualTo(310.0)
    }

    @Test
    @DisplayName("Null rawAmounts container results in empty list and zero total")
    fun nullContainerHandled() {
        val payload = WebhookPayload(
            transactionId = "tx-777",
            customerEmail = "vip@client.com",
            currency = "EUR",
            rawAmounts = null
        )

        val result = WebhookProcessor.processWebhook(payload)
        assertThat(result.validAmounts).isEmpty()
        assertThat(result.totalAmount).isEqualTo(0.0)
    }
}
