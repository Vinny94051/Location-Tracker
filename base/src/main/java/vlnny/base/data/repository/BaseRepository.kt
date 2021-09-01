package vlnny.base.data.repository

import vlnny.base.data.model.ResponseResult


abstract class BaseRepository {

    suspend fun <T, E> withErrorHandlingCall(
        call: suspend () -> T,
        errorMap: suspend (errorCode : Int?) -> E
    ): ResponseResult<T, E> =
        try {
            ResponseResult.Success(call.invoke())
        } catch (ex: Throwable) {
            ex.printStackTrace()
            ResponseResult.Error(errorMap.invoke(null))
        }

}