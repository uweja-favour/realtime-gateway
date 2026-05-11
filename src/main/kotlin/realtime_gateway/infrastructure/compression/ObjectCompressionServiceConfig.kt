package realtime_gateway.infrastructure.compression

import com.xapps.platform.core.compression.ObjectCompressionService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectCompressionServiceConfig {

    @Bean
    fun objectCompressionService(): ObjectCompressionService =
        ObjectCompressionService()
}