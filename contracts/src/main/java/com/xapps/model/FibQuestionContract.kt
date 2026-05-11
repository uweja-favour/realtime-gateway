package com.xapps.model

interface FibQuestionContract : QuestionContract


fun String.helloWorld() {
    val m = buildMap {
        put("", 1)
        put("", 2)
    }
}