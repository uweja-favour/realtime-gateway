package com.xapps.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IdHolder(
    @SerialName("id")  // ← Explicitly map to "id" in JSON
    private val _id: String
) {

    val id: String
        get() = normalizeUuid(_id)

    companion object {
        /**
         * Normalizes a string that represents a UUID by removing extraneous characters.
         *
         * This function is designed to handle both "normal" and "abnormal" input strings safely.
         * It performs the following steps:
         * 1. Returns an empty string if the input is `null` or blank.
         * 2. Trims any leading or trailing whitespace.
         * 3. Removes surrounding double quotes (`"`) or single quotes (`'`) if present.
         *
         * This ensures that input strings like the following are all normalized to the plain UUID:
         * - `"\"9d003701-d00d-4cdd-894f-51ea560fde99\""` → `"9d003701-d00d-4cdd-894f-51ea560fde99"`
         * - `"'9d003701-d00d-4cdd-894f-51ea560fde99'"` → `"9d003701-d00d-4cdd-894f-51ea560fde99"`
         * - `"9d003701-d00d-4cdd-894f-51ea560fde99"` → `"9d003701-d00d-4cdd-894f-51ea560fde99"`
         *
         * This function is safe to use on any string input and guarantees a clean, trimmed output
         * suitable for logging, storing, or UUID parsing.
         *
         * @param input The string to normalize. Can be null, blank, or contain extra quotes.
         * @return A clean, normalized UUID string with no surrounding quotes or whitespace. Returns
         * an empty string if the input is null or blank.
         */
        fun normalizeUuid(input: String?): String {
            if (input.isNullOrBlank()) return ""  // handle null or empty input gracefully

            // Remove leading/trailing whitespace, then remove surrounding quotes (single or double)
            return input.trim().trim('"').trim('\'')
        }
    }
}