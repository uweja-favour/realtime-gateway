package com.xapps.questions.contracts.question_generation

import kotlinx.serialization.Serializable

@Serializable
class JobId private constructor(val value: String) {

    init {
        require(value.isNotBlank())
    }

    companion object {
        fun of(that: String) = JobId(that)
    }
}
