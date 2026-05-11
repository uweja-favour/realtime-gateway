package com.xapps.persistence.mapper

import com.xapps.model.Option
import com.xapps.persistence.OptionDocument

fun OptionDocument.toDomain(): Option {
    return Option(
        id = id,
        questionId = questionId,
        label = label,
        text = text
    )
}

fun Option.toDocument(): OptionDocument {
    return OptionDocument(
        id = id,
        questionId = questionId,
        label = label,
        text = text
    )
}