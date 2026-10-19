package com.course.core.pricing

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PricingEngineTest {

    @Test
    @DisplayName("VIP domestic transaction has 0 fee")
    fun vipDomesticFee() {
        val fee = PricingEngine.calculateTransactionFee(amount = 1000.0, tier = "VIP", isCrossBorder = false)
        assertThat(fee).isEqualTo(0.0)
    }

    @Test
    @DisplayName("VIP cross-border transaction incurs flat 5.0 fee")
    fun vipCrossBorderFee() {
        val fee = PricingEngine.calculateTransactionFee(amount = 1000.0, tier = "VIP", isCrossBorder = true)
        assertThat(fee).isEqualTo(5.0)
    }

    @Test
    @DisplayName("Standard domestic transaction incurs 2.5% fee")
    fun standardDomesticFee() {
        val fee = PricingEngine.calculateTransactionFee(amount = 200.0, tier = "STANDARD", isCrossBorder = false)
        assertThat(fee).isEqualTo(5.0)
    }

    @Test
    @DisplayName("Gold cross-border transaction incurs 1% + 15.0 fee")
    fun goldCrossBorderFee() {
        val fee = PricingEngine.calculateTransactionFee(amount = 500.0, tier = "gold", isCrossBorder = true)
        // 500 * 0.01 = 5.0 + 15.0 = 20.0
        assertThat(fee).isEqualTo(20.0)
    }

    @Test
    @DisplayName("Invalid tier throws IllegalArgumentException")
    fun invalidTierThrows() {
        assertThatThrownBy {
            PricingEngine.calculateTransactionFee(amount = 100.0, tier = "PLATINUM_UNKNOWN")
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("Unsupported tier")
    }

    @Test
    @DisplayName("Default parameters use STANDARD tier and false crossBorder")
    fun defaultParametersWork() {
        val fee = PricingEngine.calculateTransactionFee(amount = 400.0)
        // 400 * 0.025 = 10.0
        assertThat(fee).isEqualTo(10.0)
    }

    @Test
    @DisplayName("Exponential backoff retry schedule generates correct progression")
    fun retryScheduleProgression() {
        val schedule = PricingEngine.generateRetrySchedule(maxAttempts = 4, baseDelayMs = 100L)
        assertThat(schedule).containsExactly(100L, 200L, 400L, 800L)
    }

    @Test
    @DisplayName("Rate limit check correctly detects threshold")
    fun rateLimitDetection() {
        assertThat(PricingEngine.isRateLimitExceeded("192.168.1.1", 99, 100)).isFalse()
        assertThat(PricingEngine.isRateLimitExceeded("192.168.1.1", 100, 100)).isTrue()
        assertThat(PricingEngine.isRateLimitExceeded("192.168.1.1", 105, 100)).isTrue()
    }
}
