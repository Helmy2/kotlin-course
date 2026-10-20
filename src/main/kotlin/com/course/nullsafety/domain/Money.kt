package com.course.nullsafety.domain

import java.math.BigDecimal
import java.math.RoundingMode

data class Money private constructor(
    val amount: BigDecimal,
    val currency: String
) {
    companion object {
        const val DEFAULT_CURRENCY = "EGP"

        fun of(amount: Double, currency: String = DEFAULT_CURRENCY): Money {
            require(amount >= 0.0) { "Amount must not be negative: $amount" }
            require(currency.isNotBlank()) { "Currency must not be blank" }

            val scaled = BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP)
            return Money(scaled, currency.trim().uppercase())
        }

        fun zero(currency: String = DEFAULT_CURRENCY): Money = of(0.0, currency)
    }

    operator fun plus(other: Money): Money {
        require(currency == other.currency) {
            "Currency mismatch: cannot add $currency and ${other.currency}"
        }
        return Money(amount + other.amount, currency)
    }
}
