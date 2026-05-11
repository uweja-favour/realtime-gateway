package com.xapps.messaging.kafka

object KafkaTopics {

    object Questions {
        const val REQUESTED = "questions.requested"
        const val SELF_TEST_GENERATED = "self.test.questions.generated"
        const val CLASSROOM_GENERATED = "classroom.questions.generated"
    }

    object Quiz {
        const val SELF_TEST_PAYLOAD = "new.self.test.quiz.payload"
        const val CLASSROOM_PAYLOAD = "new.classroom.quiz.payload"
        const val CLASSROOM_STATE_CHANGED = "classroom.quiz.state.changed"
    }

    object NoteSummary {
        const val NOTE_SUMMARY_PAYLOAD = "new.note.summary.payload"
    }

    object User {
        const val ONLINE = "user.online"
        const val OFFLINE = "user.offline"
    }
}