package cn.yizems.util.ktx.android.coroutine

/**
 * 支持类, 抽象 loading dialog 的支持
 */
interface ILoadingDialogSupport {
    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    fun showLoadingDialog(msg: String? = null)
    fun dismissLoadingDialog()
}