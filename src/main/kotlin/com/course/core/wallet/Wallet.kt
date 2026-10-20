package com.course.core.wallet

class Wallet(
    val walletId: String,
    val ownerName: String,
    currency: String = "EGP",
    private val validator: TransactionValidator? = null
) {
    val currency: String = currency.uppercase()

    var balance: Double = 0.0
        private set

    private val _history = mutableListOf<String>()
    val history: List<String>
        get() = _history.toList()

    init {
        require(walletId.isNotBlank()) { "walletId must not be blank" }
        require(ownerName.isNotBlank()) { "ownerName must not be blank" }
        require(this.currency in SUPPORTED_CURRENCIES) {
            "Unsupported currency: ${this.currency}. Supported: $SUPPORTED_CURRENCIES"
        }
    }

    fun credit(amount: Double, note: String = "Deposit") {
        require(amount > 0.0) { "Credit amount must be > 0.0" }
        if (validator != null) {
            require(validator.validate(amount)) { "Validation failed for credit amount: $amount" }
        }
        balance += amount
        _history.add("[CREDIT] +$amount $currency | $note")
    }

    fun debit(amount: Double, note: String = "Withdrawal") {
        require(amount > 0.0) { "Debit amount must be > 0.0" }
        check(amount <= balance) { "Insufficient funds: balance=$balance, requested=$amount" }
        if (validator != null) {
            require(validator.validate(amount)) { "Validation failed for debit amount: $amount" }
        }
        balance -= amount
        _history.add("[DEBIT] -$amount $currency | $note")
    }

    companion object {
        val SUPPORTED_CURRENCIES = setOf("EGP", "USD", "EUR")
    }
}
