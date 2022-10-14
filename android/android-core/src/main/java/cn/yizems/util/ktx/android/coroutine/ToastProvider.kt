package cn.yizems.util.ktx.android.coroutine

import androidx.annotation.StringRes

@Deprecated("use CoroutineSupportManager instead")
object ToastProvider : IToastProvider {

    fun register(toastProvider: IToastProvider) {
        CoroutineSupportManager.register(toastProvider)
    }

    override fun showShort(text: String) {
        CoroutineSupportManager.getToastProvider()?.showShort(text)
    }

    override fun showShort(resId: Int) {
        CoroutineSupportManager.getToastProvider()?.showShort(resId)
    }

    override fun showLong(text: String) {
        CoroutineSupportManager.getToastProvider()?.showLong(text)
    }

    override fun showLong(resId: Int) {
        CoroutineSupportManager.getToastProvider()?.showLong(resId)
    }
}

interface IToastProvider {
    fun showShort(text: String)
    fun showShort(@StringRes resId: Int)

    fun showLong(text: String)
    fun showLong(@StringRes resId: Int)
}
