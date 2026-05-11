package realtime_gateway.infrastructure.serialization.http_message_reader_and_writer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.http.MediaType
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.lang.Nullable
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import realtime_gateway.infrastructure.serialization.ReflectiveSerializerResolver
import java.nio.charset.StandardCharsets

class KotlinxSerializationJsonWriter(
    private val json: Json,
    private val serializerResolver: ReflectiveSerializerResolver
) : HttpMessageWriter<Any> {

    private val supportedMediaTypes = kotlin.collections.listOf(MediaType.APPLICATION_JSON)

    override fun getWritableMediaTypes(): List<MediaType> = supportedMediaTypes

    override fun canWrite(elementType: ResolvableType, @Nullable mediaType: MediaType?): Boolean {
        return mediaType == null || supportedMediaTypes.any { it.isCompatibleWith(mediaType) }
    }

    override fun write(
        inputStream: Publisher<out Any>,
        elementType: ResolvableType,
        @Nullable mediaType: MediaType?,
        message: ReactiveHttpOutputMessage,
        hints: Map<String?, Any?>
    ): Mono<Void?> {
        return Mono.from(inputStream)
            .flatMap { value ->
                Mono.fromCallable {
                    val serializer = serializerResolver.resolve(elementType) as KSerializer<Any>
                    json.encodeToString(serializer, value).toByteArray(StandardCharsets.UTF_8)
                }
                    .subscribeOn(Schedulers.boundedElastic())
                    .flatMap { bytes ->
                        message.writeWith(Mono.just(message.bufferFactory().wrap(bytes)))
                    }
            }
    }
}
