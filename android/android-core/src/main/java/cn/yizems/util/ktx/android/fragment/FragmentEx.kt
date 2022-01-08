package cn.yizems.util.ktx.android.fragment

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle


/**
 * 针对fragment 的显示,隐藏的快捷方法
 * 主要是对他们的生命周期进行自动管控
 */

/**
 * 添加 fragment 并 根据 [show] 设置最大生命周期
 *
 * @receiver FragmentTransaction
 * @param fragment Fragment
 * @param containerViewId Int
 * @param tag String?
 * @param show 是否默认显示
 * @param lifeState State 最大生命周期, 一般来说不需要设置, 根据[show]指定判定就好
 * @return FragmentTransaction
 */
fun FragmentTransaction.addFragmentAndSetLife(
    fragment: Fragment,
    @IdRes containerViewId: Int = 0,
    tag: String? = null,
    show: Boolean = false,
    lifeState: Lifecycle.State = if (show) Lifecycle.State.RESUMED else Lifecycle.State.STARTED,
): FragmentTransaction {

    this.add(containerViewId, fragment, tag)
        .setMaxLifecycle(fragment, lifeState)

    if (show) {
        show(fragment)
    } else {
        hide(fragment)
    }

    return this
}

/**
 * 显示frgment
 * 并将其他fragment 隐藏
 * 调整显示的和隐藏的fragment的MaxLifecycle
 * @receiver FragmentManager
 * @param fragment Fragment
 */
fun FragmentManager.showFragment(fragment: Fragment) {
    val fragments = this.fragments
    val transaction = this.beginTransaction()
        .show(fragment)
        .setMaxLifecycle(fragment, Lifecycle.State.RESUMED)

    fragments.forEach {
        if (it != fragment) {
            transaction.hide(it)
                .setMaxLifecycle(it, Lifecycle.State.STARTED)
        }
    }
    transaction.commitAllowingStateLoss()
}

/**
 * @see [showFragment]
 * @receiver FragmentManager
 * @param tag String
 */
fun FragmentManager.showFragmentByTag(tag: String) {
    val fragment = this.findFragmentByTag(tag) ?: return
    showFragment(fragment)
}

/**
 * 将fragment 隐藏
 * 调整隐藏的fragment的MaxLifecycle 到 [Lifecycle.State.STARTED]
 * @receiver FragmentManager
 * @param fragment Fragment
 */
fun FragmentManager.hideFragment(fragment: Fragment) {
    this.beginTransaction()
        .hide(fragment)
        .setMaxLifecycle(fragment, Lifecycle.State.STARTED)
        .commitAllowingStateLoss()
}

/**
 * @see hideFragment
 * @receiver FragmentManager
 * @param tag String
 */
fun FragmentManager.hideFragmentByTag(tag: String) {
    val fragment = this.findFragmentByTag(tag) ?: return
    hideFragment(fragment)
}

/**
 * 显示fragment 并调整fragment 到 [Lifecycle.State.RESUMED]
 * @receiver FragmentTransaction
 * @param fragment Fragment
 * @return FragmentTransaction
 */
fun FragmentTransaction.showAndMoveLife(fragment: Fragment): FragmentTransaction {
    this.show(fragment)
        .setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
        .commitAllowingStateLoss()
    return this
}

/**
 * 隐藏fragment 并调整fragment 到 [Lifecycle.State.STARTED]
 * @receiver FragmentTransaction
 * @param fragment Fragment
 * @return FragmentTransaction
 */
fun FragmentTransaction.hideAndMoveLife(fragment: Fragment): FragmentTransaction {
    this.hide(fragment)
        .setMaxLifecycle(fragment, Lifecycle.State.STARTED)
        .commitAllowingStateLoss()

    return this
}
