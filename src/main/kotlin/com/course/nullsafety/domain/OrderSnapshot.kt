package com.course.nullsafety.domain

data class OrderSnapshot(
    val orderId: String,
    val items: List<String>,
    val total: Money,
    val status: String
) {
    fun applyDiscount(discountPercent: Int): OrderSnapshot {
        require(discountPercent in 1..100) {
            "Discount percent must be between 1 and 100, got: $discountPercent"
        }

        val multiplier = (100 - discountPercent) / 100.0
        val discountedAmount = total.amount.toDouble() * multiplier
        val newTotal = Money.of(discountedAmount, total.currency)

        return this.copy(
            total = newTotal,
            status = "DISCOUNTED"
        )
    }
}
