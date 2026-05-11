package com.xapps.platform.core.outcome//@file:Suppress("unused")
//
//package com.xapps.record_android_app.outcome
//
//import kotlinx.coroutines.channels.ActorScope
//import kotlinx.coroutines.channels.SendChannel
//import kotlin.coroutines.EmptyCoroutineContext
//import kotlinx.coroutines.*
//import kotlinx.coroutines.channels.ProducerScope
//import kotlinx.coroutines.channels.ReceiveChannel
//import kotlin.coroutines.CoroutineContext
//
///**
// * Deprecated extensions to prevent bypassing TrackedScope.
// * Any attempt to call these inside a TrackedScope block will fail at compile-time.
// */
//
//@Deprecated(
//    "Use TrackedScope.launch instead of CoroutineScope.launch inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//fun CoroutineScope.launch(
//    context: CoroutineContext = EmptyCoroutineContext,
//    start: CoroutineStart = CoroutineStart.DEFAULT,
//    block: suspend CoroutineScope.() -> Unit
//): Job = error("Cannot call CoroutineScope.launch inside a TrackedScope")
//
//@Deprecated(
//    "Use TrackedScope.async instead of CoroutineScope.async inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//fun <T> CoroutineScope.async(
//    context: CoroutineContext = EmptyCoroutineContext,
//    start: CoroutineStart = CoroutineStart.DEFAULT,
//    block: suspend CoroutineScope.() -> T
//): Deferred<T> = error("Cannot call CoroutineScope.async inside a TrackedScope")
//
//@Deprecated(
//    "Use TrackedScope.produce instead of CoroutineScope.produce inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//fun <E> CoroutineScope.produce(
//    context: CoroutineContext = EmptyCoroutineContext,
//    capacity: Int = 0,
//    block: suspend ProducerScope<E>.() -> Unit
//): ReceiveChannel<E> = error("Cannot call CoroutineScope.produce inside a TrackedScope")
//
//@OptIn(ObsoleteCoroutinesApi::class)
//@Deprecated(
//    "Use TrackedScope.actor instead of CoroutineScope.actor inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//fun <E> CoroutineScope.actor(
//    context: CoroutineContext = EmptyCoroutineContext,
//    capacity: Int = 0,
//    start: CoroutineStart = CoroutineStart.DEFAULT,
//    block: suspend ActorScope<E>.() -> Unit
//): SendChannel<E> = error("Cannot call CoroutineScope.actor inside a TrackedScope")
//
//@Deprecated(
//    "Use TrackedScope.withContext instead of kotlinx.coroutines.withContext inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//suspend fun <T> CoroutineScope.withContext(
//    context: CoroutineContext,
//    block: suspend CoroutineScope.() -> T
//): T = error("Cannot call CoroutineScope.withContext inside a TrackedScope")
//
//@Deprecated(
//    "Use TrackedScope.withTimeout instead of kotlinx.coroutines.withTimeout inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//suspend fun CoroutineScope.withTimeout(
//    timeMillis: Long,
//    block: suspend CoroutineScope.() -> Unit
//): Unit = error("Cannot call CoroutineScope.withTimeout inside a TrackedScope")
//
//@Deprecated(
//    "Use TrackedScope.withTimeoutOrNull instead of kotlinx.coroutines.withTimeoutOrNull inside a TrackedScope",
//    level = DeprecationLevel.ERROR
//)
//suspend fun <T> CoroutineScope.withTimeoutOrNull(
//    timeMillis: Long,
//    block: suspend CoroutineScope.() -> T
//): T? = error("Cannot call CoroutineScope.withTimeoutOrNull inside a TrackedScope")
