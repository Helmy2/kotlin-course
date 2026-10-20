package com.course.nullsafety.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class RichDomainModelTest {

    @Test
    @DisplayName("Money factory creates properly scaled and rounded instance")
    fun moneyFactoryScaling() {
        val money = Money.of(19.994, "egp")
        assertThat(money.amount).isEqualTo(BigDecimal("19.99"))
        assertThat(money.currency).isEqualTo("EGP")

        val roundedUp = Money.of(19.995, "USD")
        assertThat(roundedUp.amount).isEqualTo(BigDecimal("20.00"))
        assertThat(roundedUp.currency).isEqualTo("USD")
    }

    @Test
    @DisplayName("Negative money amount throws IllegalArgumentException")
    fun negativeAmountThrows() {
        assertThatThrownBy {
            Money.of(-5.0)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Money plus operator aggregates amounts with matching currency")
    fun moneyAddition() {
        val m1 = Money.of(100.50, "USD")
        val m2 = Money.of(49.50, "USD")
        val total = m1 + m2

        assertThat(total.amount).isEqualTo(BigDecimal("150.00"))
        assertThat(total.currency).isEqualTo("USD")
    }

    @Test
    @DisplayName("Money plus with currency mismatch throws IllegalArgumentException")
    fun currencyMismatchThrows() {
        val egp = Money.of(100.0, "EGP")
        val usd = Money.of(100.0, "USD")

        assertThatThrownBy {
            val res = egp + usd
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Currency mismatch")
    }

    @Test
    @DisplayName("OrderSnapshot applyDiscount returns new instance with updated total and status")
    fun applyDiscountImmutability() {
        val initialOrder = OrderSnapshot(
            orderId = "ord-555",
            items = listOf("Kotlin in Action", "Clean Architecture"),
            total = Money.of(100.0, "USD"),
            status = "PENDING"
        )

        val discounted = initialOrder.applyDiscount(20)

        // Initial order unchanged
        assertThat(initialOrder.total.amount).isEqualTo(BigDecimal("100.00"))
        assertThat(initialOrder.status).isEqualTo("PENDING")

        // New discounted order
        assertThat(discounted.orderId).isEqualTo("ord-555")
        assertThat(discounted.total.amount).isEqualTo(BigDecimal("80.00"))
        assertThat(discounted.status).isEqualTo("DISCOUNTED")
    }

    @Test
    @DisplayName("Invalid discount percentages throw IllegalArgumentException")
    fun invalidDiscountThrows() {
        val order = OrderSnapshot("ord-1", listOf("Item"), Money.of(50.0), "PENDING")

        assertThatThrownBy {
            order.applyDiscount(0)
        }.isInstanceOf(IllegalArgumentException::class.java)

        assertThatThrownBy {
            order.applyDiscount(105)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }
}
