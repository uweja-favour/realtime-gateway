package com.xapps.model.attempt.record

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a user’s declared confidence level for a given answer.
 * The scale is 1–5; null means the user chose not to provide confidence.
 */
@JvmInline
value class ConfidenceCode(val value: String)

enum class AnswerConfidence(
    val code: ConfidenceCode,
    val level: Int
) {
    VERY_LOW(ConfidenceCode("very_low"), 1),
    LOW(ConfidenceCode("low"), 2),
    MEDIUM(ConfidenceCode("medium"), 3),
    HIGH(ConfidenceCode("high"), 4),
    VERY_HIGH(ConfidenceCode("very_high"), 5);

    companion object {
        private val BY_CODE: Map<ConfidenceCode, AnswerConfidence> =
            entries.associateBy { it.code }

        fun fromCodeOrNull(code: ConfidenceCode?): AnswerConfidence? =
            code?.let { BY_CODE[it] }

    }
}

/**
 * Canonical, persisted domain representation of an answer submitted for a question.
 * This is the *only* authoritative format used by the data layer.
 *
 * Variants enforce the structural requirements for each question type
 * (e.g., MC → exactly one selected option; MS → many; FIB → free text).
 *
 * Instances of this type are always considered “committed” answers.
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
sealed class Answer {
    abstract val id: String
    abstract val answerRecordId: String
    abstract val confidence: AnswerConfidence?
}

@Serializable
@SerialName("McAnswer")
data class McAnswer(
    override val id: String,
    override val answerRecordId: String,
    val selectedOptionId: String,
    override val confidence: AnswerConfidence? = null
) : Answer()

@Serializable
@SerialName("MsAnswer")
data class MsAnswer(
    override val id: String,
    override val answerRecordId: String,
    val selectedOptionsIds: Set<String> = emptySet(),
    override val confidence: AnswerConfidence? = null
) : Answer()

@Serializable
@SerialName("TfAnswer")
data class TfAnswer(
    override val id: String,
    override val answerRecordId: String,
    val selectedOptionId: String,
    override val confidence: AnswerConfidence? = null
) : Answer()

@Serializable
@SerialName("FibAnswer")
data class FibAnswer(
    override val id: String,
    override val answerRecordId: String,
    val fibTextAnswer: String,
    override val confidence: AnswerConfidence? = null
) : Answer()