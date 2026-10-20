# Lesson 07: Two-Way Java ↔ Kotlin Interoperability

> "Writing Kotlin in a polyglot JVM codebase requires deliberate use of @Jvm* annotations to ensure Java callers enjoy first-class developer experience."

---

## 🎯 Learning Objectives
1. **Handling Platform Types (`T!`)**: Recognize the unannotated Java boundary danger zone where the Kotlin compiler suspends null checks.
2. **`@JvmOverloads`**: Instruct the compiler to generate overloaded bytecode constructors and methods for Java callers.
3. **`@Throws`**: Declare checked exceptions in bytecode so Java callers can use standard `try/catch` without compilation errors.
4. **`@JvmStatic` & `@JvmField`**: Expose companion object factory methods and constants as true Java static members.

---

## 🏗️ Scenario: Enterprise Polyglot Audit Client
A legacy Java auditing framework passes unannotated `LegacyJavaAuditRecord` instances into a new Kotlin `AuditService`. Java microservices also need to instantiate and call `AuditService` directly without verbose `.INSTANCE` or `Companion` access.

---

## 📝 Refactoring Tasks
In `src/main/kotlin/com/course/interop/audit/AuditService.kt`:
1. Annotate the primary constructor with `@JvmOverloads` (`environment: String = "PRODUCTION"`, `defaultSeverity: Int = 1`).
2. Implement `logAuditRecord(record: LegacyJavaAuditRecord?): String`:
   - Annotate with `@Throws(IOException::class)`.
   - Validate that `record` and `record.eventId` are non-null and not blank; else throw `IOException("Invalid audit record: missing event ID")`.
   - Safely handle nullable Java platform type `record.actorUsername`, defaulting to `"ANONYMOUS"` if null or blank.
   - Return `"[AUDIT][$environment] ${record.eventId} by $safeUser (severity=${record.severityCode})"`.
3. In companion object:
   - Expose constant with `@JvmField val DEFAULT_SYSTEM_ACTOR = "SYSTEM_AUTOMATION"`.
   - Expose factory method with `@JvmStatic fun createLocalService(): AuditService`.

---

## 🧪 Verification
Run tests:
```bash
./gradlew test --tests "com.course.interop.audit.PolyglotAuditTest"
```
