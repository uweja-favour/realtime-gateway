package com.xapps.model

import kotlinx.serialization.Serializable

@Serializable
data class Option(
    override val id: String,
    override val label: String,
    override val text: String,
    val questionId: String
) : OptionContract