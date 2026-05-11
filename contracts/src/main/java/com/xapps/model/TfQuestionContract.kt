package com.xapps.model

interface TfQuestionContract : QuestionContract {
    val options: List<OptionContract>
}