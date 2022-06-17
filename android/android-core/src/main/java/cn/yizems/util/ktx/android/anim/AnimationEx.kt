package cn.yizems.util.ktx.android.anim

import android.view.View
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes

/**
 * 应用动画
 * @param anim 动画资源id
 */
fun View.startAnimationEx(@AnimRes anim: Int) {
    this.startAnimation(AnimationUtils.loadAnimation(context, anim))
}
