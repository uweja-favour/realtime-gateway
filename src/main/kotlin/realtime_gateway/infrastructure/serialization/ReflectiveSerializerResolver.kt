package realtime_gateway.infrastructure.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlinx.serialization.modules.SerializersModule
import org.springframework.core.ResolvableType
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KType
import kotlin.reflect.full.createType

/**
 * Reflection-based serializer resolver.
 * Zero manual registration.
 * Cached.
 * Deterministic.
 */
class ReflectiveSerializerResolver(
    private val serializersModule: SerializersModule
) {

    private val cache = ConcurrentHashMap<Type, KSerializer<*>>()

    fun resolve(resolvableType: ResolvableType): KSerializer<*> =
        resolve(resolvableType.type)

    fun resolve(javaType: Type): KSerializer<*> =
        cache.computeIfAbsent(javaType) {
            val kType = it.toKTypeOrThrow()
            serializersModule.serializer(kType)
        }

    fun Type.toKTypeOrThrow(): KType {
        return (this as? Class<*>)?.kotlin?.createType()
            ?: error(
                """
                Unable to resolve Kotlin type for $this.
                Ensure the class is Kotlin-based and @Serializable if required.
                """.trimIndent()
            )
    }
}
