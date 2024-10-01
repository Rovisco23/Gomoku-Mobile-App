package pt.isel.pdm.project.ui.common

import java.lang.IllegalStateException

sealed class LoadState <out T>
data object Idle : LoadState<Nothing>()
data object Loading : LoadState<Nothing>()
data class Loaded<T>(val result: Result<T>) : LoadState<T>()
fun <T> LoadState<T>.resetToIdle() = Idle
fun <T> LoadState<T>.loading() = Loading
fun <T> LoadState<T>.complete(result: Result<T>) = Loaded(result)

fun <T> LoadState<T>.getOrNull(): T? = when (this) {
    is Loaded -> result.getOrThrow()
    else -> throw IllegalStateException("No value available")
}

fun <T> LoadState<T>.get(): T = when (this) {
    is Loaded -> result.getOrThrow()
    else -> throw IllegalStateException("No value available")
}
