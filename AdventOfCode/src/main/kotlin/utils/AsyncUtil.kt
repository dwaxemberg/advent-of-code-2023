package utils

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun <T, R> List<T>.mapAsync(
    mapper: suspend (T) -> R
): List<R> = coroutineScope { map { async { mapper(it) } }.awaitAll() }

suspend fun <T, R> List<T>.mapAsyncIndexed(
    mapper: suspend (Int, T) -> R
): List<R> = coroutineScope { mapIndexed { index, it -> async { mapper(index, it) } }.awaitAll() }
