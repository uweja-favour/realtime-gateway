package com.xapps.platform.serialization_core

import org.slf4j.LoggerFactory

/*
    [JsonDecode] Failed to decode SelfTestQuiz | source=HttpRequest | path=/api/quiz | payload={...truncated...}
 */
object JsonLog {
    private val logger = LoggerFactory.getLogger("JsonEngine")

    // Short context like "HttpDecode", "DbDecode", "SerializerRegistry"
    fun warn(context: String, message: String, vararg meta: Pair<String, Any?>, cause: Throwable? = null) {
        val metaString = if (meta.isEmpty()) "" else meta.joinToString(", ", prefix = " | ") { "${it.first}=${it.second}" }
        logger.warn("[$context] $message$metaString", cause)
    }

    fun error(context: String, message: String, vararg meta: Pair<String, Any?>, cause: Throwable? = null) {
        val metaString = if (meta.isEmpty()) "" else meta.joinToString(", ", prefix = " | ") { "${it.first}=${it.second}" }
        logger.error("[$context] $message$metaString", cause)
    }

    fun info(context: String, message: String, vararg meta: Pair<String, Any?>) {
        val metaString = if (meta.isEmpty()) "" else meta.joinToString(", ", prefix = " | ") { "${it.first}=${it.second}" }
        logger.info("[$context] $message$metaString")
    }

    // Short helper to truncate long payloads for log readability
    fun truncate(payload: String?, max: Int = 4096): String {
        if (payload == null) return "null"
        if (payload.length <= max) return payload
        return payload.take(max) + "...(truncated, length=${payload.length})"
    }
}
