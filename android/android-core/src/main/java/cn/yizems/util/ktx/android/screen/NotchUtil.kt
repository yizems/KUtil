package cn.yizems.util.ktx.android.screen

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import cn.yizems.util.ktx.android.dimens.dp2px

/**
 * 刘海屏幕工具
 * 获取状态栏高度
 * 待完善
 */
// FIXME: 待完善
object NotchUtil {
    /**
     * 华为检测是否有刘海
     *
     * @param context
     * @return
     */
    fun hasHWNotchInScreen(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {
            Log.e("HUAWEI-NOTCH", "hasNotchInScreen ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("HUAWEI-NOTCH", "hasNotchInScreen NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("HUAWEI-NOTCH", "hasNotchInScreen Exception")
        } finally {
            return ret
        }
    }

    /**
     * 华为获取刘海平参数,就是刘海的宽 高
     *
     * @param context
     * @return
     */
    fun getHWNotchSize(context: Context): IntArray? {
        var ret = intArrayOf(0, 0)
        try {
            val cl = context.classLoader
            val HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(HwNotchSizeUtil) as IntArray
        } catch (e: ClassNotFoundException) {
            Log.e("test", "getNotchSize ClassNotFoundException")
        } catch (e: NoSuchMethodException) {
            Log.e("test", "getNotchSize NoSuchMethodException")
        } catch (e: Exception) {
            Log.e("test", "getNotchSize Exception")
        } finally {
            return ret
        }
    }
    /**
     * Android P 允许Window扩展到刘海
     *
     *
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT：仅仅当系统提供的bar完全包含了刘海区时才允许window扩展到刘海区,否则window不会和刘海区重叠
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES：允许window扩展到刘海区(原文说的是短边的刘海区, 目前有刘海的手机都在短边,所以就不纠结了)
     * LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER：不允许window扩展到刘海区。
     *
     *
     * @param window
     */
    //    public static void allowPCutout(Window window) {
    //        if (window == null) {
    //            return;
    //        }
    //        if (android.os.Build.VERSION.SDK_INT >= 28) {
    //            DisplayCutout cutout = null;
    //            WindowInsets rootWindowInsets = window.getDecorView().getRootWindowInsets();
    //            if (rootWindowInsets == null) {
    //                return;
    //            }
    //            cutout = rootWindowInsets.getDisplayCutout();
    //            if (cutout != null) {
    //                WindowManager.LayoutParams lp = window.getAttributes();
    //                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER;
    //                window.setAttributes(lp);
    //            }
    //        }
    //    }
    /**
     * 给View设置paddingTop防止 刘海挡住显示内容
     *
     * @param view
     */
    fun setPaddingTop(view: View) {
        view.setPadding(
            view.paddingLeft,
            getStatusBarHeight(),
            view.paddingRight,
            view.paddingBottom
        )
    }

    fun setHeight(view: View) {
        val layoutParams = view.layoutParams
        layoutParams.height = getStatusBarHeight()
        view.layoutParams = layoutParams
    }

    fun setHeight(view: View, realPaddingTop: Int) {
        val layoutParams = view.layoutParams
        layoutParams.height = getStatusBarHeight() + realPaddingTop.toFloat().dp2px().toInt()
        view.layoutParams = layoutParams
    }

    fun setMarginTop(view: View) {
        val sHeight: Int = getStatusBarHeight()
        if (sHeight == 0) {
            return
        }
        val layoutParams = view.layoutParams
        if (layoutParams is MarginLayoutParams) {
            layoutParams.topMargin = sHeight
            view.layoutParams = layoutParams
        }
    }

    /**
     * @param view
     * @param realMarginTop 额外的距离头部高度,为0的话,可能会跟刘海挨着,不好看
     */
    fun setMarginTop(view: View, realMarginTop: Int) {
        val sHeight: Int = getStatusBarHeight()
        if (sHeight == 0) {
            return
        }
        val layoutParams = view.layoutParams
        if (layoutParams is MarginLayoutParams) {
            layoutParams.topMargin = sHeight + realMarginTop.toFloat().dp2px().toInt()
            view.layoutParams = layoutParams
        }
    }
}
