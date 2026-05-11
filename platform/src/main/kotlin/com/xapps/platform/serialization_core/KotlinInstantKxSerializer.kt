package com.xapps.platform.serialization_core// package com.xapps.selftest.quiz.infrastructure.serialization

import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*
import kotlin.time.ExperimentalTime

/***
 * Accepts ISO-8601 textual instant.
 *
 * Accepts numeric JSON as epoch seconds or epoch millis (heuristic on length).
 *
 * Accepts object shapes with seconds/nanos, epochSecond, millis, etc.
 *
 * Accepts empty {} → returns Instant.DISTANT_PAST (safe default).
 *
 * Logs clearly and returns fallback instead of throwing.
 */
@OptIn(ExperimentalSerializationApi::class, ExperimentalTime::class)
object KotlinInstantKxSerializer : KSerializer<KotlinInstant> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("KotlinInstant", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: KotlinInstant) {
        // Canonical storage: ISO-8601 string
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): KotlinInstant {
        // We want maximal flexibility — use JsonDecoder if available
        try {
            val jsonDecoder = decoder as? JsonDecoder
            val element: JsonElement = jsonDecoder?.decodeJsonElement()
                ?: run {
                    // fallback: decoder gave a primitive; attempt to read as String
                    val raw = decoder.decodeString()
                    Json.parseToJsonElement("\"$raw\"") // wrap as string so parse logic is unified
                }

            return parseJsonElementToInstant(element)
        } catch (t: Throwable) {
            JsonLog.warn(
                "KotlinInstantDeserialize",
                "Failed to parse KotlinInstant, returning DistantPast as fallback",
                "error" to t.message
            )
            return KotlinInstant.DISTANT_PAST
        }
    }

    private fun parseJsonElementToInstant(element: JsonElement): KotlinInstant {
        try {
            when (element) {
                is JsonNull -> return KotlinInstant.DISTANT_PAST
                is JsonPrimitive -> {
                    val prim = element
                    if (prim.isString) {
                        val text = prim.content
                        // Accept ISO-8601
                        return try {
                            KotlinInstant.parse(text)
                        } catch (ex: Throwable) {
                            // Maybe numeric string representing epoch millis/seconds
                            val asLong = text.toLongOrNull()
                            when {
                                asLong == null -> {
                                    JsonLog.warn("KotlinInstantDeserialize", "String value not ISO nor epoch", "value" to text)
                                    KotlinInstant.DISTANT_PAST
                                }
                                text.length >= 13 -> KotlinInstant.fromEpochMilliseconds(asLong) // assume millis
                                else -> KotlinInstant.fromEpochSeconds(asLong) // assume seconds
                            }
                        }
                    } else if (prim.isNumeric()) {
                        // numeric JSON literal — treat as epoch millis
                        val number = prim.long
                        // Heuristic: treat as millis if large (> 10^10)
                        return if (number > 9999999999L) KotlinInstant.fromEpochMilliseconds(number) else KotlinInstant.fromEpochSeconds(number)
                    } else {
                        // Unknown primitive type - fallback
                        JsonLog.warn("KotlinInstantDeserialize", "Unknown primitive for Instant", "primitive" to prim.toString())
                        return KotlinInstant.DISTANT_PAST
                    }
                }
                is JsonObject -> {
                    // Accept several shapes:
                    // { "seconds": 123, "nanos": 0 }
                    // { "epochSecond": 123, "nanos": 0 }
                    // { } -> treat as distant past (safe)
                    if (element.isEmpty()) return KotlinInstant.DISTANT_PAST

                    fun getLong(keyCandidates: List<String>): Long? {
                        for (k in keyCandidates) {
                            val v = element[k]
                            if (v is JsonPrimitive && v.longOrNull != null) return v.long
                        }
                        return null
                    }

                    val seconds = getLong(listOf("seconds", "second", "epochSecond", "epochSeconds", "epoch_seconds", "epoch_second",  "sec", "secs", "epochSec", "epochSecs", "epochseconds", "epochsecond"))
                    val nanos = getLong(listOf("nanos", "nano", "nanoSecond", "nanoSeconds", "nanoseconds", "nanosecond", "nano_seconds", "nano_second")) ?: 0L

                    if (seconds != null) {
                        // nanos fits into Int for fromEpochSeconds
                        return KotlinInstant.fromEpochSeconds(seconds, nanos.toInt())
                    }

                    // fallback attempt: maybe they sent epochMillis as field "millis" or "epochMilli"
                    val millis = getLong(listOf("millis", "milli", "epochMilli", "epochMillis", "epoch_millis", "epoch_milli", "epochmillis", "epochmilli"))
                    if (millis != null) return KotlinInstant.fromEpochMilliseconds(millis)

                    JsonLog.warn("KotlinInstantDeserialize", "Unrecognized Instant object shape", "object" to element.toString())
                    return KotlinInstant.DISTANT_PAST
                }
                else -> {
                    JsonLog.warn("KotlinInstantDeserialize", "Unexpected JsonElement type for Instant", "element" to element.toString())
                    return KotlinInstant.DISTANT_PAST
                }
            }
        } catch (t: Throwable) {
            JsonLog.warn("KotlinInstantDeserialize", "Exception while parsing Instant", "element" to element.toString(), cause = t)
            return KotlinInstant.DISTANT_PAST
        }
    }
}

// helper extension used above
private fun JsonPrimitive.isNumeric(): Boolean = !isString && (longOrNull != null || doubleOrNull != null)
