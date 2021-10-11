package cn.yizems.util.ktx.android.view.recycleview

import androidx.recyclerview.widget.RecyclerView

class KtRvAdapterDataObserver(val change: () -> Unit) : RecyclerView.AdapterDataObserver() {
    override fun onChanged() {
        change()
    }
}