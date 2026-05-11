package com.xapps.persistence.attempt

sealed class AttemptStateDocument {

    data object NotStartedStateDocument : AttemptStateDocument()

    data class OnGoingStateDocument(
        val startedAtMillis: Long,
        val activeDurationMillis: Long,
        val lastEventAtMillis: Long,
    ) : AttemptStateDocument()

    data class PausedStateDocument(
        val startedAtMillis: Long,
        val activeDurationMillis: Long,
    ) : AttemptStateDocument()

    data class AwaitingEvaluation(
        val startedAtMillis: Long,
        val submittedAtMillis: Long,
        val activeDurationMillis: Long,
    ) : AttemptStateDocument()

    data class EvaluatedStateDocument(
        val startedAtMillis: Long,
        val submittedAtMillis: Long,
        val activeDurationMillis: Long,
        val evaluation: EvaluationDocument,
        val evaluatedAtMillis: Long
    ) : AttemptStateDocument()
}


