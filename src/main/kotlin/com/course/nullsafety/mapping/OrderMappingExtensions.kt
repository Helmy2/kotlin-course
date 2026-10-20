package com.course.nullsafety.mapping

import com.course.nullsafety.domain.Money
import com.course.nullsafety.domain.OrderSnapshot

data class RawOrderPayload(
    val rawId: String?,
    val lineItems: List<String?>?,
    val rawAmount: Double?,
    val rawCurrency: String?
)

/**
 * Extension function mapping nullable RawOrderPayload to a domain OrderSnapshot.
 * Enforces an anti-corruption layer returning Result<OrderSnapshot>.
 */
fun RawOrderPayload?.toDomainOrder(): Result<OrderSnapshot> {
    if (this == null) {
        return Result.failure(IllegalArgumentException("Payload must not be null"))
    }

    val validId = this.rawId?.trim()?.takeIf { it.isNotBlank() }
        ?: return Result.failure(IllegalArgumentException("Order ID is missing or blank"))

    val validItems = this.lineItems
        .orEmpty()
        .mapNotNull { it?.trim()?.takeIf { str -> str.isNotBlank() } }

    if (validItems.isEmpty()) {
        return Result.failure(IllegalArgumentException("Order must have at least one valid item"))
    }

    val validAmount = this.rawAmount?.takeIf { it >= 0.0 }
        ?: return Result.failure(IllegalArgumentException("Invalid order amount: ${this.rawAmount}"))

    val validCurrency = this.rawCurrency
        ?.trim()
        ?.takeIf { it.isNotBlank() }
        ?: Money.DEFAULT_CURRENCY

    val total = try {
        Money.of(validAmount, validCurrency)
    } catch (e: Exception) {
        return Result.failure(e)
    }

    val order = OrderSnapshot(
        orderId = validId,
        items = validItems,
        total = total,
        status = "CREATED"
    )

    return Result.success(order)
}
