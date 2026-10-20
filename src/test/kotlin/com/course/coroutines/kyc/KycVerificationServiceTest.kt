package com.course.coroutines.kyc

import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class KycVerificationServiceTest {

    @Test
    @DisplayName("Sequential KYC execution checks all 3 steps and takes sum of delays")
    fun sequentialKyc() = runTest {
        val start = currentTime
        val result = KycVerificationService.verifyKycSequential("200000001")
        val elapsed = currentTime - start

        assertThat(result.passed).isTrue()
        assertThat(result.details["nationalId"]).isTrue()
        assertThat(result.details["sanctions"]).isTrue()
        assertThat(result.details["creditScore"]).isTrue()
        // 300 + 400 + 350 = 1050ms
        assertThat(elapsed).isEqualTo(1050L)
    }

    @Test
    @DisplayName("Concurrent KYC execution checks all 3 steps in parallel and takes max delay")
    fun concurrentKyc() = runTest {
        val start = currentTime
        val result = KycVerificationService.verifyKycConcurrent("200000001")
        val elapsed = currentTime - start

        assertThat(result.passed).isTrue()
        assertThat(result.details["nationalId"]).isTrue()
        assertThat(result.details["sanctions"]).isTrue()
        assertThat(result.details["creditScore"]).isTrue()
        // max(300, 400, 350) = 400ms
        assertThat(elapsed).isEqualTo(400L)
    }

    @Test
    @DisplayName("Concurrent KYC correctly reports failure when sanctions check fails")
    fun sanctionsFailure() = runTest {
        // Ends with "99" -> fails sanctions
        val result = KycVerificationService.verifyKycConcurrent("200000099")

        assertThat(result.passed).isFalse()
        assertThat(result.details["nationalId"]).isTrue()
        assertThat(result.details["sanctions"]).isFalse()
    }

    @Test
    @DisplayName("Verification with sufficient timeout succeeds")
    fun timeoutSucceeds() = runTest {
        val result = KycVerificationService.verifyKycWithTimeout("200000001", timeoutMs = 600L)
        assertThat(result.passed).isTrue()
    }

    @Test
    @DisplayName("Verification exceeding timeout throws TimeoutCancellationException")
    fun timeoutExceededThrows() = runTest {
        var thrown: Throwable? = null
        try {
            // KYC concurrent takes 400ms; timeout is 250ms
            KycVerificationService.verifyKycWithTimeout("200000001", timeoutMs = 250L)
        } catch (e: Throwable) {
            thrown = e
        }
        assertThat(thrown).isInstanceOf(TimeoutCancellationException::class.java)
    }
}
