package cn.yizems.util.ktx.comm.type

/**
 * Result 加强
 * 主要是针对异常的处理
 * [onFailure] 只会执行一次
 * @param T
 * @property result Result<T>
 * @constructor
 */
class ResultWrapper<T>(
    private val result: Result<T>
) {

    /** 异常是否已处理 */
    var exceptionHandled = false

    val isSuccess: Boolean
        get() = result.isSuccess

    val isFailure: Boolean
        get() = result.isFailure

    fun onSuccess(block: (T) -> Unit): ResultWrapper<T> {
        result.onSuccess(block)
        return this
    }

    fun onFailure(block: (Throwable) -> Boolean): ResultWrapper<T> {
        result.exceptionOrNull()?.let {
            if (!exceptionHandled) {
                exceptionHandled = block(it)
            }
        }
        return this
    }


    fun getResult(): Result<T> {
        return result
    }

    fun getOrElse(default: (Throwable) -> T): T {
        return result.getOrElse(default)
    }

    fun getOrDefault(default: T): T {
        return result.getOrDefault(default)
    }

    fun getOrNull(): T? {
        return result.getOrNull()
    }

    fun getOrThrow(): T {
        return result.getOrThrow()
    }


    fun exceptionOrNull(): Throwable? {
        return result.exceptionOrNull()
    }

    override fun toString(): String {
        return result.toString()
    }

}

/**
 * 转为 ResultWrapper
 */
fun <T> Result<T>.toResultWrapper(): ResultWrapper<T> {
    return ResultWrapper(this)
}

/**
 * 异常处理: 带类型, 无异常则不执行
 *
 * 如果 [block] 返回true, 则后续的异常不会再处理
 *
 * @receiver ResultWrapper<T>
 * @return ResultWrapper<T>
 */
inline fun <T, reified R> ResultWrapper<T>.onFailureWith(block: (Throwable) -> Boolean): ResultWrapper<T> {
    exceptionOrNull()?.let {
        if (!exceptionHandled && it is R) {
            exceptionHandled = block(it)
        }
    }
    return this
}
