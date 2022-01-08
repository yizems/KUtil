package cn.yizems.util.ktx.android.keyboard

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver

/**
 * 键盘 弹起收回监听
 */
class SoftKeyBoardListener private constructor(activity: Activity) {
    private val rootView: View = activity.window.decorView//activity的根视图
    private var rootViewVisibleHeight: Int = 0//纪录根视图的显示高度
    var hide: ((Int) -> Unit)? = null
    var show: ((Int) -> Unit)? = null

    init {
        //获取activity的根视图

        //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
        rootView.viewTreeObserver.addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            //获取当前根视图在屏幕上显示的大小
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)

            val visibleHeight = r.height()
            //                System.out.println("" + visibleHeight);
            if (rootViewVisibleHeight == 0) {
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
            if (rootViewVisibleHeight == visibleHeight) {
                return@OnGlobalLayoutListener
            }

            //根视图显示高度变小超过200，可以看作软键盘显示了
            if (rootViewVisibleHeight - visibleHeight > 200) {
                show?.invoke(rootViewVisibleHeight - visibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }

            //根视图显示高度变大超过200，可以看作软键盘隐藏了
            if (visibleHeight - rootViewVisibleHeight > 200) {
                hide?.invoke(visibleHeight - rootViewVisibleHeight)
                rootViewVisibleHeight = visibleHeight
                return@OnGlobalLayoutListener
            }
        })
    }

    companion object {
        /**
         * @param activity Activity
         * @param hide ((Int) -> Unit)? 隐藏监听
         * @param show ((Int) -> Unit)? 显示监听
         */
        @JvmStatic
        fun setListener(
            activity: Activity,
            hide: ((Int) -> Unit)? = null,
            show: ((Int) -> Unit)? = null
        ) {
            val softKeyBoardListener = SoftKeyBoardListener(activity)
            softKeyBoardListener.hide = hide
            softKeyBoardListener.show = show
        }
    }
}
