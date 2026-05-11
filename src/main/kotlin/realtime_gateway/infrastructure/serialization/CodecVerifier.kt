package realtime_gateway.infrastructure.serialization

import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.http.codec.HttpMessageReader
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.stereotype.Component
import org.springframework.core.ResolvableType

@Component
class CodecVerifier {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun verifyJsonReader(readers: List<HttpMessageReader<*>>) {
        logger.info("There are firstly ${readers.size} readers.")

        val jsonReaders = readers.filter {
            it.canRead(ANY_TYPE, MediaType.APPLICATION_JSON)
        }

        logger.warn("JSON Readers: ${jsonReaders.map { it.javaClass.simpleName }}")
    }

    fun verifyJsonWriter(writers: List<HttpMessageWriter<*>>) {
        logger.info("There are firstly ${writers.size} writers.")

        val jsonWriters = writers.filter {
            it.canWrite(ANY_TYPE, MediaType.APPLICATION_JSON)
        }

        logger.warn("JSON Writers: ${jsonWriters.map { it.javaClass.simpleName }}")
    }

    companion object {
        private val ANY_TYPE = ResolvableType.forClass(Any::class.java)
    }
}
