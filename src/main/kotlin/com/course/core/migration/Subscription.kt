package com.course.core.migration

/**
 * Idiomatic Kotlin replacement for LegacySubscription.java.
 *
 * TODO: Task 1 - Refactor into an idiomatic Kotlin class (<25 lines):
 * - Primary constructor with val id, val customerId, planCode = "FREE", maxSeats = 5
 * - init block enforcing id.isNotBlank(), customerId.isNotBlank(), maxSeats >= 1
 * - Properties planCode, maxSeats, isCancelled with private set
 * - Domain methods upgrade(newPlanCode, newMaxSeats) and cancel()
 */
class Subscription(
    val id: String,
    val customerId: String
) {
    // TODO: Complete implementation
    val planCode: String = "FREE"
    val maxSeats: Int = 5
    val isCancelled: Boolean = false

    fun upgrade(newPlanCode: String, newMaxSeats: Int) {
        throw NotImplementedError("upgrade is not yet implemented")
    }

    fun cancel() {
        throw NotImplementedError("cancel is not yet implemented")
    }
}
