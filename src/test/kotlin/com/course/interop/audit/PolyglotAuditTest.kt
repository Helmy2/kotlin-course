package com.course.interop.audit

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.io.IOException
import java.lang.reflect.Modifier

class PolyglotAuditTest {

    @Test
    @DisplayName("AuditService logs valid record with actor and severity")
    fun logValidRecord() {
        val service = AuditService("STAGING", 2)
        val javaRecord = LegacyJavaAuditRecord("evt-88", "john_doe", 3)

        val log = service.logAuditRecord(javaRecord)
        assertThat(log).isEqualTo("[AUDIT][STAGING] evt-88 by john_doe (severity=3)")
    }

    @Test
    @DisplayName("Platform type with null actorUsername defaults to ANONYMOUS")
    fun platformTypeNullActor() {
        val service = AuditService()
        val javaRecord = LegacyJavaAuditRecord("evt-99", null, 1)

        val log = service.logAuditRecord(javaRecord)
        assertThat(log).isEqualTo("[AUDIT][PRODUCTION] evt-99 by ANONYMOUS (severity=1)")
    }

    @Test
    @DisplayName("Null or blank eventId throws checked IOException")
    fun invalidRecordThrowsIOException() {
        val service = AuditService()
        val invalidRecord = LegacyJavaAuditRecord("  ", "actor", 1)

        assertThatThrownBy {
            service.logAuditRecord(invalidRecord)
        }.isInstanceOf(IOException::class.java)
            .hasMessageContaining("Invalid audit record")
    }

    @Test
    @DisplayName("Bytecode verification: @JvmOverloads generates 3 constructor overloads")
    fun jvmOverloadsConstructorsExist() {
        val constructors = AuditService::class.java.constructors
        // Should have AuditService(), AuditService(String), AuditService(String, int)
        val paramCounts = constructors.map { it.parameterCount }
        assertThat(paramCounts).contains(0, 1, 2)
    }

    @Test
    @DisplayName("Bytecode verification: @Throws emits IOException in method signature")
    fun throwsAnnotationEmitsException() {
        val method = AuditService::class.java.getMethod("logAuditRecord", LegacyJavaAuditRecord::class.java)
        val exceptionTypes = method.exceptionTypes
        assertThat(exceptionTypes).contains(IOException::class.java)
    }

    @Test
    @DisplayName("Bytecode verification: @JvmStatic and @JvmField expose static members to Java")
    fun staticMembersVerification() {
        // createLocalService() must be a public static method on AuditService.class
        val method = AuditService::class.java.getMethod("createLocalService")
        assertThat(Modifier.isStatic(method.modifiers)).isTrue()

        // DEFAULT_SYSTEM_ACTOR must be a public static field on AuditService.class
        val field = AuditService::class.java.getField("DEFAULT_SYSTEM_ACTOR")
        assertThat(Modifier.isStatic(field.modifiers)).isTrue()
        assertThat(field.get(null)).isEqualTo("SYSTEM_AUTOMATION")
    }
}
