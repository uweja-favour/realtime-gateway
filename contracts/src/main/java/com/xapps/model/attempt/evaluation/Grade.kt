package com.xapps.model.attempt.evaluation

@JvmInline
value class GradeCode(val value: String)

enum class Grade(
    val code: GradeCode,
    val display: String
) {
    A_PLUS(GradeCode("a_plus"), "A+"),
    A(GradeCode("a"), "A"),
    A_MINUS(GradeCode("a_minus"), "A-"),
    B_PLUS(GradeCode("b_plus"), "B+"),
    B(GradeCode("b"), "B"),
    B_MINUS(GradeCode("b_minus"), "B-"),
    C_PLUS(GradeCode("c_plus"), "C+"),
    C(GradeCode("c"), "C"),
    C_MINUS(GradeCode("c_minus"), "C-"),
    D_PLUS(GradeCode("d_plus"), "D+"),
    D(GradeCode("d"), "D"),
    D_MINUS(GradeCode("d_minus"), "D-"),
    E(GradeCode("e"), "E"),
    F(GradeCode("f"), "F");

    companion object {
        private val BY_CODE: Map<GradeCode, Grade> =
            entries.associateBy { it.code }

        fun fromCode(code: GradeCode): Grade? =
            code.let { BY_CODE[it] }

    }
}

// Map each grade to a numeric score for fine-grained averaging
fun Grade.weight(): Double = when (this) {
    Grade.A_PLUS -> 12.0
    Grade.A       -> 11.0
    Grade.A_MINUS -> 10.0
    Grade.B_PLUS -> 9.0
    Grade.B       -> 8.0
    Grade.B_MINUS -> 7.0
    Grade.C_PLUS -> 6.0
    Grade.C       -> 5.0
    Grade.C_MINUS -> 4.0
    Grade.D_PLUS -> 3.0
    Grade.D       -> 2.0
    Grade.D_MINUS -> 1.0
    Grade.E       -> 0.5
    Grade.F       -> 0.0
}


fun Grade.toDisplayableString(): String = when (this) {
    Grade.A_PLUS -> "A+"
    Grade.A       -> "A"
    Grade.A_MINUS -> "A−"
    Grade.B_PLUS -> "B+"
    Grade.B       -> "B"
    Grade.B_MINUS -> "B−"
    Grade.C_PLUS -> "C+"
    Grade.C       -> "C"
    Grade.C_MINUS -> "C−"
    Grade.D_PLUS -> "D+"
    Grade.D       -> "D"
    Grade.D_MINUS -> "D−"
    Grade.E       -> "E"
    Grade.F       -> "F"
}