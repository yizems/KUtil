package cn.yizems.util.ktx.android.coroutine

import androidx.annotation.StringRes

object ToastProvider : IToastProvider {

    private var toastProvider: IToastProvider? = null

    fun register(toastProvider: IToastProvider) {
        ToastProvider.toastProvider = toastProvider
    }

    override fun showShort(text: String) {
        toastProvider?.showShort(text)
    }

    override fun showShort(resId: Int) {
        toastProvider?.showShort(resId)
    }

    override fun showLong(text: String) {
        toastProvider?.showLong(text)
    }

    override fun showLong(resId: Int) {
        toastProvider?.showLong(resId)
    }
}

interface IToastProvider {
    fun showShort(text: String)
    fun showShort(@StringRes resId: Int)

    fun showLong(text: String)
    fun showLong(@StringRes resId: Int)
}
