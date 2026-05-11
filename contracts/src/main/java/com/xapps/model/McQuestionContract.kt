package com.xapps.model

interface McQuestionContract : QuestionContract {
    val options: List<OptionContract>
}