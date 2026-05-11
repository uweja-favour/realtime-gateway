package com.xapps.dto

import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    val errorType: ApiErrorResponseType,
    val message: String
)

enum class ApiErrorResponseType {

    // --- Classroom / Session errors

    // SESSION STATE
    SESSION_CLOSED,
    SESSION_NOT_IN_PREVIEWABLE_STATE,
    SESSION_NOT_IN_ENROLLMENT_STATE,
    SESSION_NOT_IN_SUBMISSION_STATE,

    // SESSION CONSTRAINTS
    SESSION_CAPACITY_REACHED,
    SESSION_NOT_FOUND,
    ACTIVE_SESSION_ALREADY_EXISTS,

    // QUIZ
    CLASSROOM_QUIZ_NOT_FOUND,

    // JOIN
    INVALID_JOIN_CODE,
    PARTICIPANT_ALREADY_ENROLLED,
    PARTICIPANT_NOT_FOUND,

    // ATTEMPT
    INVALID_ATTEMPT_STATE,

    // AUTH
    TUTOR_ACCESS_DENIED,


    // --- Auth errors

    EMAIL_ALREADY_EXISTS,
    INVALID_CREDENTIALS,

    USER_BANNED,
    REFRESH_TOKEN_EXPIRED,
    REFRESH_TOKEN_REVOKED,
    REFRESH_TOKEN_INVALID,


    // --- Self Test errors
    SELF_TEST_QUIZ_NOT_FOUND,


    // --- Note Summary errors
    NOTE_SUMMARY_NOT_FOUND,
}

