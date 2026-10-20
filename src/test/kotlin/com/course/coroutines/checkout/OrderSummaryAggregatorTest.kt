package com.course.coroutines.checkout

import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class OrderSummaryAggregatorTest {

    @Test
    @DisplayName("Happy path: all 3 microservices return successfully in parallel")
    fun happyPathCheckout() = runTest {
        val start = currentTime
        val summary = OrderSummaryAggregator.aggregateCheckout(
            orderId = "ord-777",
            userId = "usr-101",
            simulateLoyaltyFailure = false,
            simulateSlowShipping = false
        )
        val elapsed = currentTime - start

        assertThat(summary.orderId).isEqualTo("ord-777")
        assertThat(summary.subtotal).isEqualTo(450.0)
        assertThat(summary.loyaltyPointsAvailable).isEqualTo(120)
        assertThat(summary.shippingFee).isEqualTo(25.0)
        assertThat(summary.warnings).isEmpty()

        // Max latency is loyalty service (250ms), proving parallel execution
        assertThat(elapsed).isEqualTo(250L)
    }

    @Test
    @DisplayName("Loyalty service failure degrades gracefully to 0 points with warning")
    fun loyaltyFailureGracefulDegradation() = runTest {
        val summary = OrderSummaryAggregator.aggregateCheckout(
            orderId = "ord-777",
            userId = "usr-101",
            simulateLoyaltyFailure = true,
            simulateSlowShipping = false
        )

        assertThat(summary.loyaltyPointsAvailable).isEqualTo(0)
        assertThat(summary.warnings).contains("Loyalty service unavailable")
        assertThat(summary.subtotal).isEqualTo(450.0)
        assertThat(summary.shippingFee).isEqualTo(25.0)
    }

    @Test
    @DisplayName("Slow shipping service times out at 300ms and falls back to flat fee 50.0")
    fun shippingTimeoutFallback() = runTest {
        val start = currentTime
        val summary = OrderSummaryAggregator.aggregateCheckout(
            orderId = "ord-777",
            userId = "usr-101",
            simulateLoyaltyFailure = false,
            simulateSlowShipping = true
        )
        val elapsed = currentTime - start

        assertThat(summary.shippingFee).isEqualTo(50.0)
        assertThat(summary.warnings).anyMatch { it.contains("timed out") }
        // Should cap at 300ms timeout
        assertThat(elapsed).isEqualTo(300L)
    }

    @Test
    @DisplayName("Critical order failure immediately aborts entire aggregation")
    fun criticalFailureAborts() = runTest {
        var thrown: Throwable? = null
        try {
            OrderSummaryAggregator.aggregateCheckout(
                orderId = "   ", // Blank ID fails critical service
                userId = "usr-101"
            )
        } catch (e: Throwable) {
            thrown = e
        }

        assertThat(thrown).isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Order ID must not be blank")
    }
}
