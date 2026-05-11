package com.xapps.platform.core.string

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
//fun generateUniqueId(): String = Uuid.generateV7().toString().take(36)
fun generateUniqueId(): String = Uuid.random().toString().take(36)