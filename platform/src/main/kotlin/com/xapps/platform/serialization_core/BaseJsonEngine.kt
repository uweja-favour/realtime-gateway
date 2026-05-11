@file:OptIn(ExperimentalTime::class)

package com.xapps.platform.serialization_core

import com.github.uwejafavour.studentapplication__.core.common.file.model.EncodedFile
import com.xapps.dto.job.JobStatus
import com.xapps.messaging.kafka.events.ClassroomQuizStateRefreshEvent
import com.xapps.model.attempt.AttemptState
import com.xapps.model.attempt.record.Answer
import com.xapps.model.attempt.record.FibAnswer
import com.xapps.model.attempt.record.McAnswer
import com.xapps.model.attempt.record.MsAnswer
import com.xapps.model.attempt.record.TfAnswer
import com.xapps.time.types.KotlinInstant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlin.time.ExperimentalTime

object BaseJsonEngine {

    val baseModule = SerializersModule {
        contextual(KotlinInstant::class, KotlinInstantKxSerializer)

        polymorphic(EncodedFile::class) {

            subclass(EncodedFile.Text::class)
            subclass(EncodedFile.Pdf::class)

            subclass(EncodedFile.Jpeg::class)
            subclass(EncodedFile.Png::class)
            subclass(EncodedFile.Gif::class)
            subclass(EncodedFile.Bmp::class)
            subclass(EncodedFile.Webp::class)

            subclass(EncodedFile.Docx::class)
            subclass(EncodedFile.Doc::class)

            subclass(EncodedFile.Xlsx::class)
            subclass(EncodedFile.Xls::class)

            subclass(EncodedFile.Pptx::class)
            subclass(EncodedFile.Ppt::class)

            subclass(EncodedFile.Binary::class)
        }

        polymorphic(Answer::class) {
            subclass(McAnswer::class)
            subclass(MsAnswer::class)
            subclass(TfAnswer::class)
            subclass(FibAnswer::class)
        }

        polymorphic(AttemptState::class) {
            subclass(AttemptState.NotStarted::class)
            subclass(AttemptState.OnGoing::class)
            subclass(AttemptState.Paused::class)
            subclass(AttemptState.AwaitingEvaluation::class)
            subclass(AttemptState.Evaluated::class)
        }

        polymorphic(JobStatus::class) {
            subclass(JobStatus.Queued::class)
            subclass(JobStatus.Running::class)
            subclass(JobStatus.Completed::class)
            subclass(JobStatus.Failed::class)
            subclass(JobStatus.Cancelled::class)
        }

        polymorphic(ClassroomQuizStateRefreshEvent::class) {
            subclass(ClassroomQuizStateRefreshEvent.ParticipantRefreshQuizzes::class)
            subclass(ClassroomQuizStateRefreshEvent.QuizRefreshParticipants::class)
            subclass(ClassroomQuizStateRefreshEvent.Tutor::class)
        }
    }

    val json: Json =
        Json {
            serializersModule = baseModule

            // LENIENT + RESILIENT settings
            ignoreUnknownKeys = true        // extra fields won't fail decoding
            isLenient = true                // accept slightly malformed JSON (e.g., single quotes)
            coerceInputValues = true        // missing/nullable values can be coerced rather than failing
            allowSpecialFloatingPointValues = true
            encodeDefaults = false
            prettyPrint = false
            explicitNulls = false
            classDiscriminator = "type"
        }

}
