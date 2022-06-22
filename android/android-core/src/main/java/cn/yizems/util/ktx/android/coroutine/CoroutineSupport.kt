package cn.yizems.util.ktx.android.coroutine

import androidx.annotation.StringRes
import cn.yizems.util.ktx.android.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 协程支持类
 *
 * 主要服务于 Presenter 层的协程 或者 Activity 层的协程 或者 Fragment 层的协程 或者 Dialog 层的协程
 */
interface CoroutineSupport {

    /**
     * 默认主线程, 子类需要实现
     */
    val coroutineScope: CoroutineScope

    /**
     * 执行协程
     *
     * @param showHint Boolean 是否显示默认 提示
     * @param defaultErrorToast Int 默认错误提示信息
     * @param autoShowLoading Boolean 是否自动显示弹窗, 子类可实现[ILoadingSupport] 来决定如何显示 loading,如果不知线,则不会自动显示 loading
     * @param autoDismissLoading Boolean 是否自动取消弹窗
     * @param context CoroutineContext 额外的协程控制,比如指定线程,或者错误处理器
     * @param start CoroutineStart 启动模式
     * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, Unit>
     */
    @Suppress("NAME_SHADOWING")
    fun launch(
        showHint: Boolean = true,
        defaultErrorToast: Int = R.string.toast_network_error,
        loadingMsg: String? = null,
        autoShowLoading: Boolean = true,
        autoDismissLoading: Boolean = true,
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        var context = context

        context += DefaultCoroutineExceptionHandler(defaultErrorToast, showHint, null)

        coroutineScope.launch(context, start) {

            val imp = this@CoroutineSupport

            if (autoShowLoading && imp is ILoadingSupport) {
                imp.showLoading(loadingMsg)
            }

            try {
                block()
            } catch (e: Exception) {
                //发生错误时关闭loading
                if (imp is ILoadingSupport) {
                    imp.dismissLoading()
                }
                throw e
            } finally {
                if (autoDismissLoading && imp is ILoadingSupport) {
                    imp.dismissLoading()
                }
            }
        }
    }

    /**
     * 静默执行, 无 toast,无 loading
     *
     * @param errorCallback CoroutineErrorCallback?
     * @param context CoroutineContext
     * @param start CoroutineStart
     * @param block [@kotlin.ExtensionFunctionType] SuspendFunction1<CoroutineScope, Unit>
     */
    fun launchSilent(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) = launch(
        false,
        R.string.toast_network_error,
        null,
        false,
        false,
        context,
        start,
        block
    )


    /**
     * 异步执行
     */
    fun asyncSimple(block: suspend CoroutineScope.() -> Unit) {
        coroutineScope.launch {
            withContext(Dispatchers.Default, block)
        }
    }

    /**
     * 延迟执行
     * @param time Long 毫秒
     * @param action Function0<Unit>
     */
    fun display(time: Long, action: () -> Unit) {
        coroutineScope.launch {
            delay(time)
            action()
        }
    }

    /**
     * 主线程执行
     * @param action Function0<Unit>
     */
    fun uiThread(action: () -> Unit) {
        coroutineScope.launch(context = Dispatchers.Main) {
            action()
        }
    }

    /**
     * 设置 错误回调 最好提前配置, 即在启动协程后第一时间去配置
     * 注意: 必须是通过 [CoroutineSupport.launch] 启动的协程才可以使用此方法
     * @see [CoroutineErrorCallback.callback]
     * @receiver CoroutineScope
     */
    fun CoroutineScope.onError(
        @StringRes msgId: Int? = null,
        showHint: Boolean? = null,
        coroutineErrorCallback: CoroutineErrorCallback? = null,
    ) {
        val exceptionHandler =
            coroutineContext[CoroutineExceptionHandler] as? DefaultCoroutineExceptionHandler
                ?: throw IllegalAccessException("必须是通过 [CoroutineSupport] 创建的协程才可以使用该方法")

        if (msgId != null) {
            exceptionHandler.msgId = msgId
        }

        if (showHint != null) {
            exceptionHandler.showHint = showHint
        }

        if (coroutineErrorCallback != null) {
            exceptionHandler.coroutineErrorCallback = coroutineErrorCallback
        }
    }

    /**
     * 设置 错误回调 最好提前配置, 即在启动协程后第一时间去配置
     * @see onError
     * @see [CoroutineErrorCallback.callback]
     * @receiver CoroutineScope
     * @param callback Function1<Throwable, Unit>
     */
    fun CoroutineScope.onError(callback: (Throwable) -> Any?) {
        onError(null, null, CoroutineErrorCallback(null, null, callback))
    }


    suspend fun <T> withIOContext(
        block: suspend CoroutineScope.() -> T
    ) = withContext(Dispatchers.IO, block)

    suspend fun <T> withMainContext(
        block: suspend CoroutineScope.() -> T
    ) = withContext(Dispatchers.Main, block)

}
