package cn.yizems.util.ktx.android.view

import android.view.Window

fun Window.setAlpha(alpha: Float) {
    val at = this.attributes
    at.alpha = alpha
    this.attributes = at
}