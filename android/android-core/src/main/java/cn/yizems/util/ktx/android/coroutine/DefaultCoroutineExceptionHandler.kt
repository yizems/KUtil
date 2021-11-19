package cn.yizems.util.ktx.android.coroutine

import androidx.annotation.StringRes
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

/**
 * 协程自动捕获异常, 会吧错误信息toast 出来
 *
 * @param msg 默认提示文字的资源id
 * @param showHint: 是否需要toast
 */
class DefaultCoroutineExceptionHandler(
    @StringRes var msgId: Int = 0,
    var showHint: Boolean = true,
    var coroutineErrorCallback: CoroutineErrorCallback? = null,
) : AbstractCoroutineContextElement(CoroutineExceptionHandler), CoroutineExceptionHandler {

    override fun handleException(context: CoroutineContext, exception: Throwable) {
//        KLog.e(exception)
        context.cancel()

        val intercept = coroutineErrorCallback?.callback?.invoke(exception)

        if (intercept == true) {
            return
        }


        if (exception is CoroutineParentException) {
            handleCoroutineToastException(exception)
        } else {
            if (coroutineErrorCallback?.interceptToast?.invoke(exception) != true) {
                toast()
            }
            if (coroutineErrorCallback?.interceptUpload?.invoke(exception) != true) {
                upload(exception)
            }
        }
    }


    private fun handleCoroutineToastException(exception: CoroutineParentException) {
        if (exception.showToast() && coroutineErrorCallback?.interceptToast?.invoke(exception) != true) {
            toast(exception.toastMsg())
        }
        if (exception.needUpload() && coroutineErrorCallback?.interceptUpload?.invoke(exception) != true) {
            upload(exception)
        }
    }

    private fun toast(exceptionMsg: String? = null) {
        if (!showHint) {
            return
        }
        // TODO: 2021/10/26
        if (exceptionMsg != null) {
//            ToastCompat.makeText(ContextHolder.get(), exceptionMsg, ToastCompat.DURATION_LONG).show()
        } else if (msgId != 0 && showHint) {
//            ToastCompat.makeText(ContextHolder.get(), msgId, ToastCompat.DURATION_LONG).show()
        }
    }

    private fun upload(exception: Throwable) {
        // TODO: 05/03/2021 异常上报
    }
}

/**
 * 用于 协程异常统一处理
 * @constructor
 */
abstract class CoroutineParentException(msg: String) : Exception(msg) {
    /** 是否toast */
    open fun showToast(): Boolean = true

    /** toast 信息 */
    abstract fun toastMsg(): String?

    /** 是否需要异常,当做未知异常处理: 后续可以加错误上传策略 */
    open fun needUpload(): Boolean = false
}

/** 只发送 toast 的异常 */
class ToastException(msg: String) : CoroutineParentException(msg) {
    override fun toastMsg(): String? = message
}

/**
 * 协程错误的时候回调
 * @property interceptToast Function1<Throwable, Boolean?>? 是否拦截toast
 * @property interceptUpload Function1<Throwable, Boolean?>? 是否拦截 上传事件
 * @property callback Function1<Throwable, Any?> 回调, 如果返回 true, 则代表拦截该错误,默认异常处理器不再处理
 * @constructor
 */
class CoroutineErrorCallback(
    val interceptToast: ((Throwable) -> Boolean?)? = null,
    val interceptUpload: ((Throwable) -> Boolean?)? = null,
    val callback: (Throwable) -> Any?,
)