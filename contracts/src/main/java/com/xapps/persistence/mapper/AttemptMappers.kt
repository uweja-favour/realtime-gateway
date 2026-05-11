package com.xapps.persistence.mapper

import com.xapps.model.attempt.AttemptState
import com.xapps.model.attempt.evaluation.Evaluation
import com.xapps.model.attempt.evaluation.Grade
import com.xapps.model.attempt.evaluation.QuestionOutcome
import com.xapps.model.attempt.evaluation.Report
import com.xapps.model.attempt.evaluation.TopicAnalysis
import com.xapps.model.attempt.record.Answer
import com.xapps.model.attempt.record.AnswerConfidence
import com.xapps.model.attempt.record.AnswerRecord
import com.xapps.model.attempt.record.FibAnswer
import com.xapps.model.attempt.record.McAnswer
import com.xapps.model.attempt.record.MsAnswer
import com.xapps.model.attempt.record.TfAnswer
import com.xapps.persistence.attempt.AnswerRecordDocument
import com.xapps.persistence.attempt.AttemptStateDocument
import com.xapps.persistence.attempt.EvaluationDocument
import com.xapps.persistence.attempt.QuestionOutcomeDocument
import com.xapps.persistence.attempt.ReportDocument
import com.xapps.persistence.attempt.TopicAnalysisDocument
import com.xapps.persistence.attempt.answer.AnswerDocument
import com.xapps.persistence.attempt.answer.FibAnswerDocument
import com.xapps.persistence.attempt.answer.McAnswerDocument
import com.xapps.persistence.attempt.answer.MsAnswerDocument
import com.xapps.persistence.attempt.answer.TfAnswerDocument
import com.xapps.time.types.KotlinInstant

fun AnswerRecord.toDocument(): AnswerRecordDocument {
    return AnswerRecordDocument(
        id = id,
        attemptId = attemptId,
        questionId = questionId,
        answer = answer.toDocument()
    )
}

private fun Answer.toDocument(): AnswerDocument {
    return when (this) {
        is FibAnswer -> FibAnswerDocument(
            id = id,
            answerRecordId = answerRecordId,
            fibTextAnswer = fibTextAnswer,
            confidenceCode = confidence?.code
        )
        is McAnswer -> McAnswerDocument(
            id = id,
            answerRecordId = answerRecordId,
            selectedOptionId = selectedOptionId,
            confidenceCode = confidence?.code
        )
        is MsAnswer -> MsAnswerDocument(
            id = id,
            answerRecordId = answerRecordId,
            selectedOptionIds = selectedOptionsIds,
            confidenceCode = confidence?.code
        )
        is TfAnswer -> TfAnswerDocument(
            id = id,
            answerRecordId = answerRecordId,
            selectedOptionId = selectedOptionId,
            confidenceCode = confidence?.code
        )
    }
}

fun AttemptState.toDocument(): AttemptStateDocument {
    return when (this) {
        AttemptState.NotStarted -> AttemptStateDocument.NotStartedStateDocument
        is AttemptState.OnGoing -> AttemptStateDocument.OnGoingStateDocument(
            startedAtMillis = startedAt.toEpochMilliseconds(),
            activeDurationMillis = activeDurationMillis,
            lastEventAtMillis = lastEventAt.toEpochMilliseconds()
        )
        is AttemptState.Paused -> AttemptStateDocument.PausedStateDocument(
            startedAtMillis = startedAt.toEpochMilliseconds(),
            activeDurationMillis = activeDurationMillis
        )
        is AttemptState.AwaitingEvaluation -> AttemptStateDocument.AwaitingEvaluation(
            startedAtMillis = startedAt.toEpochMilliseconds(),
            submittedAtMillis = submittedAt.toEpochMilliseconds(),
            activeDurationMillis = activeDurationMillis
        )
        is AttemptState.Evaluated -> AttemptStateDocument.EvaluatedStateDocument(
            startedAtMillis = startedAt.toEpochMilliseconds(),
            submittedAtMillis = submittedAt.toEpochMilliseconds(),
            activeDurationMillis = activeDurationMillis,
            evaluation = evaluation.toDocument(),
            evaluatedAtMillis = evaluatedAt.toEpochMilliseconds()
        )
    }
}

private fun Evaluation.toDocument(): EvaluationDocument {
    return EvaluationDocument(
        id = id,
        attemptId = attemptId,
        outcomes = outcomes.map { it.toDocument() },
        gradeCode = grade.code,
        report = report.toDocument(),
        evaluatedAtMillis = evaluatedAtMillis
    )
}

private fun QuestionOutcome.toDocument(): QuestionOutcomeDocument {
    return QuestionOutcomeDocument(
        id = id,
        evaluationId = evaluationId,
        questionId = questionId,
        isCorrect = isCorrect
    )
}

private fun Report.toDocument(): ReportDocument {
    return ReportDocument(
        id = id,
        evaluationId = evaluationId,
        overallAccuracy = overallAccuracy,
        overallConfidence = overallConfidence,
        topicAnalysis = topicAnalysis.map { it.toDocument() }
    )
}

private fun TopicAnalysis.toDocument(): TopicAnalysisDocument {
    return TopicAnalysisDocument(
        id = id,
        reportId = reportId,
        topic = topic,
        averageConfidence = averageConfidence,
        accuracy = accuracy,
        questionCount = questionCount,
        correctCount = correctCount
    )
}

fun AnswerRecordDocument.toDomain(): AnswerRecord {
    return AnswerRecord(
        id = id,
        attemptId = attemptId,
        questionId = questionId,
        answer = answer.toDomain()
    )
}

private fun AnswerDocument.toDomain(): Answer {
    return when (this) {

        is FibAnswerDocument ->
            FibAnswer(
                id = id,
                answerRecordId = answerRecordId,
                fibTextAnswer = fibTextAnswer,
                confidence = AnswerConfidence.fromCodeOrNull(confidenceCode)
            )

        is McAnswerDocument ->
            McAnswer(
                id = id,
                answerRecordId = answerRecordId,
                selectedOptionId = selectedOptionId,
                confidence = AnswerConfidence.fromCodeOrNull(confidenceCode)
            )

        is MsAnswerDocument ->
            MsAnswer(
                id = id,
                answerRecordId = answerRecordId,
                selectedOptionsIds = selectedOptionIds,
                confidence = AnswerConfidence.fromCodeOrNull(confidenceCode)
            )

        is TfAnswerDocument ->
            TfAnswer(
                id = id,
                answerRecordId = answerRecordId,
                selectedOptionId = selectedOptionId,
                confidence = AnswerConfidence.fromCodeOrNull(confidenceCode)
            )
    }
}

fun AttemptStateDocument.toDomain(): AttemptState {
    return when (this) {

        AttemptStateDocument.NotStartedStateDocument ->
            AttemptState.NotStarted

        is AttemptStateDocument.OnGoingStateDocument ->
            AttemptState.OnGoing(
                startedAt = KotlinInstant.fromEpochMilliseconds(startedAtMillis),
                activeDurationMillis = activeDurationMillis,
                lastEventAt = KotlinInstant.fromEpochMilliseconds(lastEventAtMillis)
            )

        is AttemptStateDocument.PausedStateDocument ->
            AttemptState.Paused(
                startedAt = KotlinInstant.fromEpochMilliseconds(startedAtMillis),
                activeDurationMillis = activeDurationMillis
            )

        is AttemptStateDocument.AwaitingEvaluation ->
            AttemptState.AwaitingEvaluation(
                startedAt = KotlinInstant.fromEpochMilliseconds(startedAtMillis),
                submittedAt = KotlinInstant.fromEpochMilliseconds(submittedAtMillis),
                activeDurationMillis = activeDurationMillis
            )

        is AttemptStateDocument.EvaluatedStateDocument ->
            AttemptState.Evaluated(
                startedAt = KotlinInstant.fromEpochMilliseconds(startedAtMillis),
                submittedAt = KotlinInstant.fromEpochMilliseconds(submittedAtMillis),
                evaluatedAt = KotlinInstant.fromEpochMilliseconds(evaluatedAtMillis),
                activeDurationMillis = activeDurationMillis,
                evaluation = evaluation.toDomain()
            )
    }
}

private fun EvaluationDocument.toDomain(): Evaluation {
    return Evaluation(
        id = id,
        attemptId = attemptId,
        outcomes = outcomes.map { it.toDomain() },
        grade = Grade.fromCode(gradeCode)
            ?: error("Invalid GradeCode: $gradeCode"),
        report = report.toDomain(),
        evaluatedAtMillis = evaluatedAtMillis
    )
}

private fun QuestionOutcomeDocument.toDomain(): QuestionOutcome {
    return QuestionOutcome(
        id = id,
        evaluationId = evaluationId,
        questionId = questionId,
        isCorrect = isCorrect
    )
}

private fun ReportDocument.toDomain(): Report {
    return Report(
        id = id,
        evaluationId = evaluationId,
        overallAccuracy = overallAccuracy,
        overallConfidence = overallConfidence,
        topicAnalysis = topicAnalysis.map { it.toDomain() }
    )
}

private fun TopicAnalysisDocument.toDomain(): TopicAnalysis {
    return TopicAnalysis(
        id = id,
        reportId = reportId,
        topic = topic,
        averageConfidence = averageConfidence,
        accuracy = accuracy,
        questionCount = questionCount,
        correctCount = correctCount
    )
}