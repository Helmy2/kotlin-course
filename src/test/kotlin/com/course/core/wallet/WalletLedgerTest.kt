package com.course.core.wallet

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class WalletLedgerTest {

    @Test
    @DisplayName("Default wallet initializes with zero balance and EGP currency")
    fun defaultInitialization() {
        val wallet = Wallet(walletId = "w-001", ownerName = "Ahmed")
        assertThat(wallet.walletId).isEqualTo("w-001")
        assertThat(wallet.ownerName).isEqualTo("Ahmed")
        assertThat(wallet.currency).isEqualTo("EGP")
        assertThat(wallet.balance).isEqualTo(0.0)
        assertThat(wallet.history).isEmpty()
    }

    @Test
    @DisplayName("Unsupported currency throws IllegalArgumentException")
    fun unsupportedCurrency() {
        assertThatThrownBy {
            Wallet(walletId = "w-002", ownerName = "Sara", currency = "JPY")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported currency")
    }

    @Test
    @DisplayName("Credit updates balance and appends to history")
    fun creditSuccess() {
        val wallet = Wallet(walletId = "w-003", ownerName = "Tarek", currency = "USD")
        wallet.credit(250.0, "Salary Deposit")

        assertThat(wallet.balance).isEqualTo(250.0)
        assertThat(wallet.history).hasSize(1)
        assertThat(wallet.history[0]).contains("[CREDIT]", "+250.0", "USD", "Salary Deposit")
    }

    @Test
    @DisplayName("Debit updates balance and logs withdrawal")
    fun debitSuccess() {
        val wallet = Wallet(walletId = "w-004", ownerName = "Mona")
        wallet.credit(500.0)
        wallet.debit(150.0, "ATM Withdrawal")

        assertThat(wallet.balance).isEqualTo(350.0)
        assertThat(wallet.history).hasSize(2)
        assertThat(wallet.history[1]).contains("[DEBIT]", "-150.0", "EGP", "ATM Withdrawal")
    }

    @Test
    @DisplayName("Debit exceeding balance throws IllegalStateException")
    fun debitInsufficientFunds() {
        val wallet = Wallet(walletId = "w-005", ownerName = "Ali")
        wallet.credit(100.0)

        assertThatThrownBy {
            wallet.debit(150.0)
        }.isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("Insufficient funds")
    }

    @Test
    @DisplayName("Transaction validator rejection throws IllegalArgumentException")
    fun validatorRejection() {
        val strictValidator = TransactionValidator { amount -> amount <= 1000.0 }
        val wallet = Wallet(walletId = "w-006", ownerName = "Karim", validator = strictValidator)

        assertThatThrownBy {
            wallet.credit(1500.0)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Validation failed")
    }

    @Test
    @DisplayName("History list is defensively copied and cannot mutate wallet state")
    fun historyImmutability() {
        val wallet = Wallet(walletId = "w-007", ownerName = "Nour")
        wallet.credit(100.0)

        val retrievedHistory = wallet.history
        assertThat(retrievedHistory).hasSize(1)
        // retrievedHistory is List<String> (read-only)
    }
}
