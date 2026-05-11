package com.xapps.dto.job

import com.xapps.questions.contracts.question_generation.JobId
import kotlinx.serialization.Serializable

@Serializable
data class JobDTO(
    val jobId: JobId,
    val task: JobTask,
    val status: JobStatus
)

@JvmInline
value class JobTaskCode(val value: String)

enum class JobTask(val code: JobTaskCode, val displayName: String) {
    SELF_TEST(JobTaskCode("self_test"), "Self Test Task"),
    CLASSROOM(JobTaskCode("classroom"), "Classroom Task");

    companion object {
        private val BY_CODE = entries.associateBy { it.code }

        fun fromCodeOrNull(code: JobTaskCode): JobTask? =
            BY_CODE[code]
    }
}