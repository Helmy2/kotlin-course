package com.course.interop.audit

import java.io.IOException

class AuditService @JvmOverloads constructor(
    val environment: String = "PRODUCTION",
    val defaultSeverity: Int = 1
) {
    /**
     * Logs an audit record from Java, safely handling unannotated platform types
     * and declaring checked IOException in bytecode for Java callers.
     */
    @Throws(IOException::class)
    fun logAuditRecord(record: LegacyJavaAuditRecord?): String {
        if (record == null) {
            throw IOException("Invalid audit record: record must not be null")
        }

        val eventId = record.eventId?.trim()?.takeIf { it.isNotBlank() }
            ?: throw IOException("Invalid audit record: missing event ID")

        // Safely handle unannotated Java platform type (String!)
        val actor: String? = record.actorUsername
        val safeActor = actor?.trim()?.takeIf { it.isNotBlank() } ?: "ANONYMOUS"

        val severity = if (record.severityCode > 0) record.severityCode else defaultSeverity

        return "[AUDIT][$environment] $eventId by $safeActor (severity=$severity)"
    }

    companion object {
        @JvmField
        val DEFAULT_SYSTEM_ACTOR: String = "SYSTEM_AUTOMATION"

        @JvmStatic
        fun createLocalService(): AuditService {
            return AuditService("LOCAL", 0)
        }
    }
}
