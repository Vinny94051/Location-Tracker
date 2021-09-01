package vlnny.base.data.model


sealed class ResponseResult<out T, out E> {
    data class Success<out T>(val value: T) : ResponseResult<T, Nothing>()

    data class Error<out E>(val error: E? = null) : ResponseResult<Nothing, E>()
}

fun <I, E, O> ResponseResult<I, E>.mapIfSuccess(
    mapRule: (I) -> ResponseResult.Success<O>
): ResponseResult<O, E> {
    return when (this) {
        is ResponseResult.Success<I> -> mapRule(this.value)
        is ResponseResult.Error -> this
    }
}

suspend fun <T, E> ResponseResult<T, E>.doOnSuccess(
    action: suspend (T) -> Unit
): ResponseResult<T, E> {
    when (this) {
        is ResponseResult.Success -> action.invoke(this.value)
    }
    return this
}

suspend fun <T, E> ResponseResult<T, E>.doOnError(
    action: suspend (E?) -> Unit
): ResponseResult<T, E> {
    when (this) {
        is ResponseResult.Error -> action.invoke(this.error)
    }
    return this
}

fun <V, E> ResponseResult<V, E>.successValue(): V? {
    return when (this) {
        is ResponseResult.Success<V> -> this.value
        is ResponseResult.Error -> null
    }
}

fun <T, E> ResponseResult<T, E>.error(): E? {
    return when (this) {
        is ResponseResult.Success -> null
        is ResponseResult.Error -> this.error
    }
}

