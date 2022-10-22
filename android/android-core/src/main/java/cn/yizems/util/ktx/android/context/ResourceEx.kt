package cn.yizems.util.ktx.android.context

import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat

/**
 * 获取颜色: 通过ResourcesCompat方式获取
 */
fun getColorCompat(@ColorRes colorId: Int): Int {
    return ResourcesCompat.getColor(getGlobalContext().resources, colorId, getGlobalContext().theme)
}
