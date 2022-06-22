@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.android.view

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.Px

/**
 * 是对于 ktx 的补充, ktx 中有的方法,这里没有实现
 */

/**
 * [doOnPreDraw] 的扩展,action 返回值决定是否取消绘制
 * @receiver View
 * @param action Function1<[@kotlin.ParameterName] View, Boolean> 返回true,代表继续绘制
 */
inline fun View.doOnPreDrawEx(crossinline action: (view: View) -> Boolean) {
    val vto = viewTreeObserver
    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            val continueDraw = action(this@doOnPreDrawEx)
            when {
                vto.isAlive -> vto.removeOnPreDrawListener(this)
                else -> viewTreeObserver.removeOnPreDrawListener(this)
            }
            return continueDraw
        }
    })
}

/**
 * 获取焦点
 * 会设置 [View.isFocusableInTouchMode] [View.isEnabled] 为 true
 */
fun View.requestFocusEx() {
    this.isFocusable = true
    this.isFocusableInTouchMode = true
    this.isEnabled = true
    this.requestFocus()
}

/**
 * 设置 View 的 margin
 */
inline fun View.updateMargins(
    @Px left: Int? = null,
    @Px top: Int? = null,
    @Px right: Int? = null,
    @Px bottom: Int? = null
) {
    val lp = this.layoutParams ?: return
    if (lp is ViewGroup.MarginLayoutParams) {
        lp.leftMargin = left ?: lp.leftMargin
        lp.topMargin = top ?: lp.topMargin
        lp.rightMargin = right ?: lp.rightMargin
        lp.bottomMargin = bottom ?: lp.bottomMargin
        this.layoutParams = lp
    } else {
        Log.e("ViewGroup_updateMargins", "非MarginLayoutParams")
    }
}
