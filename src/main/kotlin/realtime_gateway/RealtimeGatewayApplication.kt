package realtime_gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories

@SpringBootApplication(
    exclude = [
        JacksonAutoConfiguration::class,
        org.springframework.boot.autoconfigure.r2dbc.R2dbcAutoConfiguration::class
    ]
)
class RealtimeGatewayApplication

fun main(args: Array<String>) {
    runApplication<RealtimeGatewayApplication>(*args)
}

// ./gradlew :realtime-gateway:bootRun --args='--spring.profiles.active=dev'