package com.xapps.platform.core.extensions

/**
 * Converts this [Int] megabyte (MB) value to its equivalent size in bytes.
 *
 * @receiver the size in **megabytes (MB)**
 * @return the equivalent size in **bytes**, as an [Int]
 *
 * Example:
 * ```kotlin
 * val limit = 200.mb // 200 MB in bytes
 * ```
 */
val Int.mb: Int
    get() = this * 1024 * 1024

/**
 * Converts this [Int] kilobyte (KB) value to its equivalent size in bytes.
 *
 * @receiver the size in **kilobytes (KB)**
 * @return the equivalent size in **bytes**, as an [Int]
 */
val Int.kb: Int
    get() = this * 1024

/**
 * Converts this [Int] gigabyte (GB) value to its equivalent size in bytes.
 *
 * ⚠️ Use this only when you are sure the resulting size fits within [Long] range.
 *
 * @receiver the size in **gigabytes (GB)**
 * @return the equivalent size in **bytes**, as a [Long]
 *
 * Example:
 * ```kotlin
 * val maxLimit = 2.gb // 2 GB in bytes (Long)
 * ```
 */
val Int.gb: Long
    get() = this.toLong() * 1024 * 1024 * 1024

// --------------------------------------------------------------------------
// Inverse conversions (for logging, display, or metrics visualization)
// --------------------------------------------------------------------------

/**
 * Converts this [Long] byte value into its equivalent size in **kilobytes (KB)**.
 *
 * @receiver the size in **bytes**
 * @return the equivalent size in **kilobytes (KB)** as a [Long]
 */
val Long.bytesToKb: Long
    get() = this / 1024

val Int.bytesToKb: Long
    get() = this.toLong().bytesToKb

/**
 * Converts this [Long] byte value into its equivalent size in **megabytes (MB)**.
 *
 * @receiver the size in **bytes**
 * @return the equivalent size in **megabytes (MB)** as a [Long]
 *
 * Example:
 * ```kotlin
 * val sizeInMb = totalSize.bytesToMb
 * println("File size: $sizeInMb MB")
 * ```
 */
val Long.bytesToMb: Long
    get() = this / (1024 * 1024)

/**
 * Converts this [Long] byte value into its equivalent size in **gigabytes (GB)**.
 *
 * @receiver the size in **bytes**
 * @return the equivalent size in **gigabytes (GB)** as a [Double]
 */
val Long.bytesToGb: Double
    get() = this.toDouble() / (1024.0 * 1024.0 * 1024.0)
