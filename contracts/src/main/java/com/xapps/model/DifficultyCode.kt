package com.xapps.model

// IMPORTANT: NEVER ALTER THE CODE
@JvmInline
value class DifficultyCode(val value: String)

enum class Difficulty(
    val code: DifficultyCode,
    val weight: Double,
    val displayName: String,
    val description: String
) {
    VERY_EASY(DifficultyCode("very_easy"), 1.0, "Very Easy", "Basic recall, little to no challenge"),
    EASY(DifficultyCode("easy"), 2.0, "Easy", "Simple understanding, low challenge"),
    MEDIUM(DifficultyCode("medium"), 3.0, "Medium", "Moderate complexity, requires reasoning"),
    HARD(DifficultyCode("hard"), 4.0, "Hard", "Advanced, requires multi-step reasoning"),
    VERY_HARD(DifficultyCode("very_hard"), 5.0, "Very Hard", "Expert-level, high cognitive load");

    companion object {
        fun fromWeight(weight: Double): Difficulty =
            when {
                weight < 1.5 -> VERY_EASY
                weight < 2.5 -> EASY
                weight < 3.5 -> MEDIUM
                weight < 4.5 -> HARD
                else -> VERY_HARD
            }

        private val BY_CODE = entries.associateBy { it.code }

        fun fromCodeOrNull(code: DifficultyCode): Difficulty? =
            BY_CODE[code]
    }
}
