package com.course.nullsafety.mapping

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class OrderMappingExtensionsTest {

    @Test
    @DisplayName("Null receiver returns Result.failure")
    fun nullReceiverReturnsFailure() {
        val payload: RawOrderPayload? = null
        val result = payload.toDomainOrder()

        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull())
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Payload must not be null")
    }

    @Test
    @DisplayName("Valid payload maps successfully into domain OrderSnapshot")
    fun validPayloadMapping() {
        val payload = RawOrderPayload(
            rawId = "ord-100",
            lineItems = listOf("Kotlin Book", " ", null, "Keyboard"),
            rawAmount = 149.99,
            rawCurrency = "usd"
        )

        val result = payload.toDomainOrder()

        assertThat(result.isSuccess).isTrue()
        val order = result.getOrThrow()
        assertThat(order.orderId).isEqualTo("ord-100")
        assertThat(order.items).containsExactly("Kotlin Book", "Keyboard")
        assertThat(order.total.amount).isEqualTo(BigDecimal("149.99"))
        assertThat(order.total.currency).isEqualTo("USD")
        assertThat(order.status).isEqualTo("CREATED")
    }

    @Test
    @DisplayName("Blank or missing ID returns Result.failure")
    fun invalidIdReturnsFailure() {
        val payload = RawOrderPayload(
            rawId = "   ",
            lineItems = listOf("Item"),
            rawAmount = 50.0,
            rawCurrency = "EGP"
        )
        val result = payload.toDomainOrder()
        assertThat(result.isFailure).isTrue()
    }

    @Test
    @DisplayName("Empty or completely invalid items list returns Result.failure")
    fun emptyItemsReturnsFailure() {
        val payload = RawOrderPayload(
            rawId = "ord-101",
            lineItems = listOf("  ", null),
            rawAmount = 50.0,
            rawCurrency = "EGP"
        )
        val result = payload.toDomainOrder()
        assertThat(result.isFailure).isTrue()
        assertThat(result.exceptionOrNull()?.message).contains("at least one valid item")
    }

    @Test
    @DisplayName("Negative or null amount returns Result.failure")
    fun invalidAmountReturnsFailure() {
        val payloadNullAmount = RawOrderPayload("ord-102", listOf("Item"), null, "EGP")
        assertThat(payloadNullAmount.toDomainOrder().isFailure).isTrue()

        val payloadNegAmount = RawOrderPayload("ord-103", listOf("Item"), -10.0, "EGP")
        assertThat(payloadNegAmount.toDomainOrder().isFailure).isTrue()
    }

    @Test
    @DisplayName("Missing currency defaults to EGP")
    fun currencyDefaulting() {
        val payload = RawOrderPayload("ord-104", listOf("Item"), 25.0, null)
        val result = payload.toDomainOrder()

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow().total.currency).isEqualTo("EGP")
    }
}
