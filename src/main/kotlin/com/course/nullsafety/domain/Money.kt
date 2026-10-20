package com.course.nullsafety.domain

import java.math.BigDecimal

data class Money(
    val amount: BigDecimal,
    val currency: String
) {
    // TODO: Task 1 - Make primary constructor private
    // TODO: Task 2 - Add companion object with of() and zero() factories enforcing 2-decimal scale
    // TODO: Task 3 - Implement operator fun plus(other: Money): Money

    companion object {
        const val DEFAULT_CURRENCY = "EGP"

        fun of(amount: Double, currency: String = DEFAULT_CURRENCY): Money {
            throw NotImplementedError("of() factory is not yet implemented")
        }

        fun zero(currency: String = DEFAULT_CURRENCY): Money {
            throw NotImplementedError("zero() factory is not yet implemented")
        }
    }

    operator fun plus(other: Money): Money {
        throw NotImplementedError("plus operator is not yet implemented")
    }
}
