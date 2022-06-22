package cn.yizems.util.ktx.android.coroutine.demo

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import cn.yizems.util.ktx.android.coroutine.CoroutineSupport
import cn.yizems.util.ktx.android.coroutine.ILoadingSupport
import cn.yizems.util.ktx.android.coroutine.ToastException
import cn.yizems.util.ktx.android.coroutine.ToastProvider
import kotlinx.coroutines.*

private class BaseActivity
    : AppCompatActivity(), CoroutineSupport, ILoadingSupport {
    /**
     * 默认主线程, 子类需要实现
     */
    override val coroutineScope: CoroutineScope
        get() = lifecycle.coroutineScope

    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    override fun showLoading(msg: String?) {
        // 显示弹窗
    }

    override fun dismissLoading() {
        // 关闭弹窗
    }


    private fun biz() {
        // 具体业务逻辑
        launch {
            delay(5_000)
            ToastProvider.showShort("业务完成")
        }
        launch(showHint = true) {

            onError {
                if (it is IllegalArgumentException) {
                    // 返回 true, 这种异常会被拦截, 需要你自己处理这里的逻辑, 不会 toast 提示
                    return@onError true
                }
                // 返回 false, 代表不拦截
                return@onError false
            }

            delay(5_000)

            // 这种类型的异常,以及子类, 会被外部捕获, 并自动toast出来
            // 和 [showHint] 参数有关, 如果设置为false, 则不会 toast 提示
            throw ToastException("业务异常")
        }
    }

}

private class BasePresenter(private val mView: Any?) : CoroutineSupport, ILoadingSupport {

    /**
     * 默认主线程, 子类需要实现
     */
    override val coroutineScope: CoroutineScope
        get() = CoroutineScope((SupervisorJob() + Dispatchers.Main))

    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    override fun showLoading(msg: String?) {
//        mView?.showLoading(msg)
    }

    override fun dismissLoading() {
//        mView?.dismissLoading()
    }

    fun detach() {
        coroutineScope.cancel()
    }
}

private class BaseDialog(context: Context, private val lifecycle: Lifecycle) : Dialog(context),
    CoroutineSupport, ILoadingSupport {


    /**
     * 默认主线程, 子类需要实现
     */
    override val coroutineScope: CoroutineScope
        get() = lifecycle.coroutineScope


    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    override fun showLoading(msg: String?) {
        // 显示dialog中的loading视图
    }

    override fun dismissLoading() {
        // 隐藏dialog中的loading视图
    }

    override fun dismiss() {
        super.dismiss()
    }
}
