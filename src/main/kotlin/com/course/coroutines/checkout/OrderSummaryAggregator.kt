package com.course.coroutines.checkout

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withTimeoutOrNull

data class CheckoutSummary(
    val orderId: String,
    val subtotal: Double,
    val loyaltyPointsAvailable: Int,
    val shippingFee: Double,
    val warnings: List<String>
)

object OrderSummaryAggregator {

    suspend fun fetchOrderSubtotal(orderId: String): Double {
        delay(200)
        require(orderId.isNotBlank()) { "Order ID must not be blank" }
        return 450.0
    }

    suspend fun fetchLoyaltyPoints(userId: String, simulateFailure: Boolean): Int {
        delay(250)
        if (simulateFailure) {
            throw RuntimeException("Loyalty DB Timeout")
        }
        return 120
    }

    suspend fun estimateShippingFee(orderId: String, slowNetwork: Boolean): Double {
        if (slowNetwork) {
            delay(800)
        } else {
            delay(150)
        }
        return 25.0
    }

    /**
     * Resiliently aggregates downstream services in parallel using supervisorScope.
     */
    suspend fun aggregateCheckout(
        orderId: String,
        userId: String,
        simulateLoyaltyFailure: Boolean = false,
        simulateSlowShipping: Boolean = false
    ): CheckoutSummary = supervisorScope {
        val warnings = mutableListOf<String>()

        // 1. Critical service
        val orderDeferred = async {
            fetchOrderSubtotal(orderId)
        }

        // 2. Non-critical service with error resilience
        val loyaltyDeferred = async {
            try {
                fetchLoyaltyPoints(userId, simulateLoyaltyFailure)
            } catch (e: Exception) {
                null
            }
        }

        // 3. Non-critical service with timeout SLA
        val shippingDeferred = async {
            withTimeoutOrNull(300L) {
                estimateShippingFee(orderId, simulateSlowShipping)
            }
        }

        // Critical call propagates failure if broken
        val subtotal = orderDeferred.await()

        val points = loyaltyDeferred.await()
        val finalPoints = if (points != null) {
            points
        } else {
            warnings.add("Loyalty service unavailable")
            0
        }

        val shipping = shippingDeferred.await()
        val finalShipping = if (shipping != null) {
            shipping
        } else {
            warnings.add("Shipping fee estimation timed out, applied default flat rate")
            50.0
        }

        CheckoutSummary(
            orderId = orderId,
            subtotal = subtotal,
            loyaltyPointsAvailable = finalPoints,
            shippingFee = finalShipping,
            warnings = warnings
        )
    }
}
