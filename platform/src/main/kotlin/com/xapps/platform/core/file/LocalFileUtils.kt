package com.xapps.platform.core.file

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

/**
 * Creates a temporary **file** with the given [suffix], executes [block] with it,
 * and guarantees deletion of the file immediately after the block completes (successfully or not).
 *
 * This is the most **scoped, functional** form of temporary file usage:
 * the file exists only within the duration of [block], and is deleted right after.
 *
 * ### Behavior
 * - The file is created in the system temp directory (via [Files.createTempFile]).
 * - The file is deleted immediately after [block] finishes (even if an exception is thrown).
 * - No cleanup is deferred until JVM shutdown.
 *
 * ### When to use
 * - You need a single temporary file for transient work (e.g., OCR, image processing, file conversions).
 * - You want **automatic cleanup** and **scoped lifetime** (no residue).
 *
 * ### When not to use
 * - You need to preserve the file for later inspection (use [Files.createTempFile] manually instead).
 *
 * ### Example
 * ```
 * val text = withTempFile(".txt") { temp ->
 *     temp.writeText("hello world")
 *     temp.readText()
 * }
 * ```
 */
public inline fun <R> withTempFile(suffix: String = ".tmp", block: (File) -> R): R {
    val tempFile = Files.createTempFile("tmp--${System.currentTimeMillis()}", suffix).toFile()
    return try {
        block(tempFile)
    } finally {
        runCatching { tempFile.delete() }
    }
}

/**
 * Creates a temporary **file path** (as [Path]) with the given [suffix],
 * executes [block] with it, and schedules its deletion **upon JVM exit**.
 *
 * This version is similar to [withTempFile], but:
 * - Exposes a [Path] instead of a [File].
 * - The cleanup is **deferred** until the JVM terminates.
 *
 * ### Behavior
 * - The file is created in the system temp directory.
 * - [deleteOnExit] is called, meaning deletion occurs *later*, not immediately.
 * - This is useful for debugging or when the file must outlive the function scope.
 *
 * ### When to use
 * - You want to keep the temporary file available for later use in the same process.
 * - You don’t want it to vanish immediately after the function returns.
 *
 * ### When not to use
 * - You need guaranteed immediate cleanup (use [withTempFile] instead).
 *
 * ### Example
 * ```
 * val reportPath = withTempPath(".pdf") { path ->
 *     Files.writeString(path, "generated report content")
 *     path
 * }
 * ```
 */
public inline fun <R> withTempPath(suffix: String = ".tmp", block: (Path) -> R): R {
    val tempPath = Files.createTempFile("tmp-", suffix)
    return try {
        block(tempPath)
    } finally {
        runCatching { tempPath.toFile().deleteOnExit() }
    }
}

/**
 * Creates a temporary **directory** (via [Files.createTempDirectory]),
 * executes [block] with it, and ensures recursive deletion of the directory and its contents
 * immediately after [block] completes (successfully or not).
 *
 * ### Behavior
 * - The directory and all files within it are deleted **right after** [block] finishes.
 * - No cleanup is deferred until JVM shutdown.
 * - Safe even if the block throws an exception.
 *
 * ### When to use
 * - You need a temporary workspace for multiple related files (e.g., extracted archives, batch processing).
 * - You want **immediate cleanup** after the block completes.
 *
 * ### When not to use
 * - You want to inspect or reuse the directory later (use [Files.createTempDirectory] manually or [persistentTempDir]).
 *
 * ### Example
 * ```
 * withTempDir { dir ->
 *     val tempFile = dir.resolve("example.txt").toFile()
 *     tempFile.writeText("Temporary data")
 *     println("Created file at: ${tempFile.absolutePath}")
 * }
 * // dir and all contents are deleted now
 * ```
 */
public inline fun <R> withTempDir(prefix: String = "tmp-", block: (Path) -> R): R {
    val dir = Files.createTempDirectory(prefix)
    return try {
        block(dir)
    } finally {
        runCatching { dir.toFile().deleteRecursively() }
    }
}

/**
 * Creates a **persistent temporary directory** that remains available
 * until the JVM exits (cleanup is deferred via [File.deleteOnExit]).
 *
 * Unlike [withTempDir], the directory is **not deleted immediately**;
 * it persists for the lifetime of the JVM process, allowing reuse or late inspection.
 *
 * ### Behavior
 * - The directory is created under the system temp folder.
 * - Marked for deletion on JVM shutdown.
 * - You are responsible for manual cleanup if you want early removal.
 *
 * ### When to use
 * - You need a long-lived temp directory for caching, staging, or large file pipelines.
 * - You may want to inspect intermediate files later (e.g., debugging extraction results).
 *
 * ### When not to use
 * - You need automatic cleanup after a short-lived operation (use [withTempDir] instead).
 *
 * ### Example
 * ```
 * val tempDir = persistentTempDir("quiz-file-")
 * val file = tempDir.resolve("input.pdf").toFile()
 * file.writeBytes(uploadedBytes)
 * // tempDir persists until JVM exit
 * ```
 */
public fun persistentTempDir(prefix: String = "persistent-"): Path =
    Files.createTempDirectory("${prefix}${System.currentTimeMillis()}").apply {
        toFile().deleteOnExit()
    }

/**
 * Creates a **persistent temporary file** that remains until JVM shutdown.
 *
 * This complements [persistentTempDir] for cases where you only need one file,
 * not a full directory workspace.
 *
 * ### Behavior
 * - The file is created in the system temp directory.
 * - Deletion is deferred via [deleteOnExit].
 * - No automatic cleanup after scope.
 *
 * ### When to use
 * - You need to hand off the file path to another thread, subsystem, or async process.
 * - The file must survive beyond the current function call.
 *
 * ### When not to use
 * - You want automatic cleanup after use (use [withTempFile]).
 *
 * ### Example
 * ```
 * val tempFile = persistentTempFile(".csv")
 * tempFile.writeText("id,name\n1,Jane Doe")
 * // remains until JVM exits
 * ```
 */
public fun persistentTempFile(suffix: String = ".tmp"): File =
    Files.createTempFile("persistent-", suffix).toFile().apply {
        deleteOnExit()
    }



/**
 * Returns the lowercase file extension (without the dot) from a given filename,
 * or `null` if the name has no extension.
 *
 * Examples:
 * ```
 * "report.pdf".extensionOrNull()        // "pdf"
 * "archive.tar.gz".extensionOrNull()    // "gz"
 * "README".extensionOrNull()            // null
 * null.extensionOrNull()                // null
 * ```
 */
fun String?.extensionOrNull(): String? =
    this
        ?.substringAfterLast('.', missingDelimiterValue = "")
        ?.takeIf { it.isNotBlank() }
        ?.lowercase()

///**
// * Returns the lowercase file extension (without the dot) of the uploaded file,
// * or `null` if the filename is missing or has no extension.
// */
//fun MultipartFile.extensionOrNull(): String? =
//    originalFilename.extensionOrNull()