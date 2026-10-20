package com.course.coroutines.checkout

data class CheckoutSummary(
    val orderId: String,
    val subtotal: Double,
    val loyaltyPointsAvailable: Int,
    val shippingFee: Double,
    val warnings: List<String>
)

object OrderSummaryAggregator {

    /**
     * Aggregates downstream services in parallel using supervisorScope.
     *
     * TODO: Implement resilient aggregation:
     * 1. Core Order (critical): fails aggregation if orderId is blank or service throws
     * 2. Loyalty (non-critical): fallback to 0 points on failure + warning "Loyalty service unavailable"
     * 3. Shipping (non-critical SLA): timeout at 300ms, fallback to 50.0 + warning "Shipping fee estimation timed out, applied default flat rate"
     */
    suspend fun aggregateCheckout(
        orderId: String,
        userId: String,
        simulateLoyaltyFailure: Boolean = false,
        simulateSlowShipping: Boolean = false
    ): CheckoutSummary {
        throw NotImplementedError("aggregateCheckout is not yet implemented")
    }
}
