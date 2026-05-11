@file: OptIn(ExperimentalTime::class, ExperimentalSerializationApi::class)

package realtime_gateway.infrastructure.serialization

import com.xapps.platform.serialization_core.BaseJsonEngine
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import realtime_gateway.infrastructure.messaging.websocket.model.ServerWebSocketMessage
import kotlin.time.ExperimentalTime

object RealtimeGatewayJsonEngine {

    val module =
        SerializersModule {
            // register other contextual serializers as needed via startup registration

            polymorphic(ServerWebSocketMessage::class) {
                subclass(ServerWebSocketMessage.NewSelfTestQuizPayload::class)
                subclass(ServerWebSocketMessage.NewClassroomQuizPayload::class)
                subclass(ServerWebSocketMessage.NewNoteSummaryPayload::class)

                subclass(ServerWebSocketMessage.RefreshTutorClassroomQuizPayload::class)
                subclass(ServerWebSocketMessage.PingClient::class)
                subclass(ServerWebSocketMessage.RefreshParticipantClassroomQuizzesPayload::class)
                subclass(ServerWebSocketMessage.RefreshParticipantClassroomQuizPayload::class)
            }
        }

    val json: Json =
        Json {
            serializersModule = module + BaseJsonEngine.baseModule

            // LENIENT + RESILIENT settings
            ignoreUnknownKeys = true        // extra fields won't fail decoding
            isLenient = true                // accept slightly malformed JSON (e.g., single quotes)
            coerceInputValues = true        // missing/nullable values can be coerced rather than failing
            allowSpecialFloatingPointValues = true
            encodeDefaults = false
            prettyPrint = false
            explicitNulls = false
            classDiscriminator = "type"
        }
}
