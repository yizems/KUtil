package cn.yizems.util.ktx.android.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * 更加安全的 adapter 实现, 配合 [IFragmentItem] 使用
 * @property items List<IFragmentItem>
 */
class SafePagerAdapter : FragmentStateAdapter {

    private val items: List<IFragmentItem>

    constructor(fragmentActivity: FragmentActivity, items: List<IFragmentItem>)
            : super(fragmentActivity) {
        this.items = items
    }

    constructor(fragment: Fragment, items: List<IFragmentItem>) : super(fragment) {
        this.items = items
    }

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        items: List<IFragmentItem>
    ) : super(fragmentManager, lifecycle) {
        this.items = items
    }

    private fun getItems() = items

    override fun getItemCount(): Int {
        return items.size
    }

    override fun createFragment(position: Int): Fragment {
        return items[position].create(position)
    }
}
