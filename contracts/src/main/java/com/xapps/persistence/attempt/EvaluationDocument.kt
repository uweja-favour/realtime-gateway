package com.xapps.persistence.attempt

import com.xapps.model.attempt.evaluation.GradeCode

data class EvaluationDocument(
    val id: String,
    val attemptId: String,
    val outcomes: List<QuestionOutcomeDocument>,
    val gradeCode: GradeCode,
    val report: ReportDocument,
    val evaluatedAtMillis: Long
)