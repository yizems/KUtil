package cn.yizems.util.ktx.android.context

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

fun Activity.hideSoftInput() {
    var view = currentFocus
    if (view == null) view = View(this)
    val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.isSoftShowing(): Boolean {
    //获取当前屏幕内容的高度
    //获取当前屏幕内容的高度
    val screenHeight: Int = window.decorView.height
    //获取View可见区域的bottom
    //获取View可见区域的bottom
    val rect = Rect()
    //DecorView即为activity的顶级view
    //DecorView即为activity的顶级view
    window.decorView.getWindowVisibleDisplayFrame(rect)
    //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
    //选取screenHeight*2/3进行判断  3/4
    //考虑到虚拟导航栏的情况（虚拟导航栏情况下：screenHeight = rect.bottom + 虚拟导航栏高度）
    //选取screenHeight*2/3进行判断  3/4
    return screenHeight * 3 / 4 > rect.bottom
}