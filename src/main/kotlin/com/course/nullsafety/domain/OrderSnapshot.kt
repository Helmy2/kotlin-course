package com.course.nullsafety.domain

data class OrderSnapshot(
    val orderId: String,
    val items: List<String>,
    val total: Money,
    val status: String
) {
    /**
     * Applies a percentage discount and transitions status to "DISCOUNTED".
     *
     * TODO: Use .copy() to return a new OrderSnapshot without mutating this instance.
     */
    fun applyDiscount(discountPercent: Int): OrderSnapshot {
        throw NotImplementedError("applyDiscount is not yet implemented")
    }
}
