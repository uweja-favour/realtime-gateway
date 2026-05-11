package com.xapps.model

interface MsQuestionContract : QuestionContract {
    val options: List<Option>
}



fun String.helloWorld2() {
    val m = buildMap {
        put("", 1)
        put("", 2)
    }
}