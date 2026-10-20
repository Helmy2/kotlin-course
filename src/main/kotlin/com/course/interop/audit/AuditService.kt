package com.course.interop.audit

import java.io.IOException

// TODO: Task 1 - Annotate constructor with @JvmOverloads
class AuditService(
    val environment: String = "PRODUCTION",
    val defaultSeverity: Int = 1
) {
    /**
     * Logs an audit record from Java, throwing checked IOException on invalid records.
     *
     * TODO: Task 2 - Add @Throws(IOException::class) and handle platform type nulls safely
     */
    fun logAuditRecord(record: LegacyJavaAuditRecord?): String {
        throw NotImplementedError("logAuditRecord is not yet implemented")
    }

    companion object {
        // TODO: Task 3 - Add @JvmField
        val DEFAULT_SYSTEM_ACTOR: String = "SYSTEM_AUTOMATION"

        // TODO: Task 4 - Add @JvmStatic
        fun createLocalService(): AuditService {
            return AuditService("LOCAL", 0)
        }
    }
}
