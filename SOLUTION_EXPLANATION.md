# Solution Explanation: Concept 07 - Polyglot JVM Interop

## Architectural Decisions & Kotlin Idioms

### 1. The Platform Type Hazard (`T!`)
When Kotlin calls unannotated Java methods like `record.getActorUsername()`, the compiler assigns the type `String!`. This means the compiler disables compile-time null checks because it cannot determine nullability from Java bytecode.
```kotlin
val actor: String? = record.actorUsername
val safeActor = actor?.trim()?.takeIf { it.isNotBlank() } ?: "ANONYMOUS"
```
By explicitly declaring the receiving type as nullable (`String?`), we re-engage Kotlin's null safety guarantees.

### 2. Constructor Telescoping with `@JvmOverloads`
Kotlin functions with default parameters compile to a single method in bytecode by default. Java callers cannot see default parameters. By annotating the constructor with `@JvmOverloads`, the compiler generates:
- `AuditService()`
- `AuditService(String environment)`
- `AuditService(String environment, int defaultSeverity)`

### 3. Checked Exceptions with `@Throws`
Kotlin does not have checked exceptions. Without `@Throws(IOException::class)`, Java code attempting `try { service.logAuditRecord(...); } catch (IOException e)` will fail to compile with `error: exception IOException is never thrown in body of corresponding try statement`. `@Throws` generates the `throws IOException` clause in the classfile bytecode.

### 4. `@JvmStatic` & `@JvmField`
- `@JvmStatic` synthesizes a real `static` method on `AuditService.class` so Java can write `AuditService.createLocalService()`.
- `@JvmField` exposes `DEFAULT_SYSTEM_ACTOR` as a raw public static field without getters.
