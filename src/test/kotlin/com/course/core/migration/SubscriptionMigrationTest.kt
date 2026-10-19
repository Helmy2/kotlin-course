package com.course.core.migration

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class SubscriptionMigrationTest {

    @Test
    @DisplayName("Default subscription has FREE tier and 5 seats")
    fun defaultSubscription() {
        val sub = Subscription("sub-101", "cust-900")
        assertThat(sub.id).isEqualTo("sub-101")
        assertThat(sub.customerId).isEqualTo("cust-900")
        assertThat(sub.planCode).isEqualTo("FREE")
        assertThat(sub.maxSeats).isEqualTo(5)
        assertThat(sub.isCancelled).isFalse()
    }

    @Test
    @DisplayName("Custom planCode is normalized to uppercase")
    fun customPlanCode() {
        val sub = Subscription("sub-102", "cust-901", "pro", 10)
        assertThat(sub.planCode).isEqualTo("PRO")
        assertThat(sub.maxSeats).isEqualTo(10)
    }

    @Test
    @DisplayName("Blank id or customerId throws IllegalArgumentException")
    fun blankIdThrows() {
        assertThatThrownBy {
            Subscription("  ", "cust-900")
        }.isInstanceOf(IllegalArgumentException::class.java)

        assertThatThrownBy {
            Subscription("sub-101", "")
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Seat count under 1 throws IllegalArgumentException")
    fun invalidSeatsThrows() {
        assertThatThrownBy {
            Subscription("sub-101", "cust-900", "PRO", 0)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Upgrade updates plan and seats successfully")
    fun upgradeSubscription() {
        val sub = Subscription("sub-101", "cust-900", "FREE", 5)
        sub.upgrade("enterprise", 25)
        assertThat(sub.planCode).isEqualTo("ENTERPRISE")
        assertThat(sub.maxSeats).isEqualTo(25)
    }

    @Test
    @DisplayName("Upgrade cannot downgrade seats")
    fun cannotDowngradeSeats() {
        val sub = Subscription("sub-101", "cust-900", "PRO", 20)
        assertThatThrownBy {
            sub.upgrade("ENTERPRISE", 15)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("Cancelled subscription cannot be upgraded")
    fun cancelledCannotUpgrade() {
        val sub = Subscription("sub-101", "cust-900")
        sub.cancel()
        assertThat(sub.isCancelled).isTrue()

        assertThatThrownBy {
            sub.upgrade("PRO", 10)
        }.isInstanceOf(IllegalStateException::class.java)
    }
}
