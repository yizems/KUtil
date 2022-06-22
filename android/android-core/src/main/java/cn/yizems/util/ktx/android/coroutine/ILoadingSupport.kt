package cn.yizems.util.ktx.android.coroutine

/**
 * 支持类, 抽象 loading dialog 的支持
 */
interface ILoadingSupport {
    /**
     * @param msg String? loading 上提示的文字, 例如:提交中
     */
    fun showLoading(msg: String? = null)
    fun dismissLoading()
}
