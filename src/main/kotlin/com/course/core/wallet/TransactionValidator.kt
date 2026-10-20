package com.course.core.wallet

fun interface TransactionValidator {
    fun validate(amount: Double): Boolean
}
