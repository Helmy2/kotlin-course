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
 *
 * TODO: Implement anti-corruption validation returning Result<OrderSnapshot>:
 * 1. Return failure if receiver is null
 * 2. Return failure if rawId is null or blank
 * 3. Filter lineItems (strip nulls and blanks); return failure if empty
 * 4. Return failure if rawAmount is null or < 0.0
 * 5. Normalize currency (fallback to Money.DEFAULT_CURRENCY)
 * 6. Construct OrderSnapshot and return Result.success(order)
 */
fun RawOrderPayload?.toDomainOrder(): Result<OrderSnapshot> {
    throw NotImplementedError("toDomainOrder extension function is not yet implemented")
}
