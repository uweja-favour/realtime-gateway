package com.xapps.model

@JvmInline
value class TaskDraftStatusCode(val value: String)

enum class TaskDraftStatus(val code: TaskDraftStatusCode, val displayName: String) {
    COMPLETED(TaskDraftStatusCode("completed"), "Completed"),
    IN_PROGRESS(TaskDraftStatusCode("in_progress"), "In Progress"),
    FAILED(TaskDraftStatusCode("failed"), "Failed"),
    UNKNOWN(TaskDraftStatusCode("unknown"), "Unknown");

    companion object {
        private val byCode = entries.associateBy { it.code }

        fun fromCode(code: TaskDraftStatusCode): TaskDraftStatus {
            return byCode[code] ?: UNKNOWN
        }
    }
}