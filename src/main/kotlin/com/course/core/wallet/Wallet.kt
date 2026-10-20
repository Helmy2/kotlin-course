package com.course.core.wallet

class Wallet(
    val walletId: String,
    val ownerName: String,
    currency: String = "EGP",
    private val validator: TransactionValidator? = null
) {
    // TODO: Task 1 - Normalize currency to uppercase and validate against {"EGP", "USD", "EUR"}
    val currency: String = currency.uppercase()

    // TODO: Task 2 - Encapsulate balance with private set
    var balance: Double = 0.0

    // TODO: Task 3 - Implement backing property for history
    val history: List<String> = emptyList()

    fun credit(amount: Double, note: String = "Deposit") {
        // TODO: Validate positive amount, check validator, update balance and append history
        throw NotImplementedError("credit is not yet implemented")
    }

    fun debit(amount: Double, note: String = "Withdrawal") {
        // TODO: Validate positive amount and sufficient funds, check validator, update balance and append history
        throw NotImplementedError("debit is not yet implemented")
    }
}
