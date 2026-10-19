package com.course.core.migration

class Subscription(
    val id: String,
    val customerId: String,
    planCode: String = "FREE",
    maxSeats: Int = 5
) {
    var planCode: String = planCode.uppercase()
        private set

    var maxSeats: Int = maxSeats
        private set

    var isCancelled: Boolean = false
        private set

    init {
        require(id.isNotBlank()) { "id required" }
        require(customerId.isNotBlank()) { "customerId required" }
        require(maxSeats >= 1) { "maxSeats must be >= 1" }
    }

    fun upgrade(newPlanCode: String, newMaxSeats: Int) {
        check(!isCancelled) { "Cannot upgrade cancelled subscription" }
        require(newMaxSeats >= this.maxSeats) { "Downgrade not allowed via upgrade()" }
        this.planCode = newPlanCode.uppercase()
        this.maxSeats = newMaxSeats
    }

    fun cancel() {
        this.isCancelled = true
    }
}
