package cn.yizems.util.ktx.android.view.recycleview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.yizems.util.ktx.android.context.ContextHolder
import cn.yizems.util.ktx.android.dimens.dp2px

/**
 * RecycleView.LinearLayoutManager 单颜色分割线
 * @param startPaddingPx 开头间距
 * @param endPaddingPx 结尾间距
 * @param dividerHeightPx 分割线高度
 * @param dividerColor 分割线颜色
 * @param skipPosition 是否跳过某个位置
 */
class ColorLinearLayoutItemDecoration(
    private val startPaddingPx: Float = 0F,
    private val endPaddingPx: Float = 0F,
    private val dividerHeightPx: Float = ContextHolder.me().dp2px(0.5F).toFloat(),
    private val dividerColor: Int,
    private val skipPosition: ((position: Int) -> Boolean)? = null
) : RecyclerView.ItemDecoration() {


    private val dividerPaint by lazy {
        Paint().apply {
            this.color = dividerColor
            this.style = Paint.Style.FILL
        }
    }

    private fun getOrientation(parent: RecyclerView): Orientation {
        val layoutManager = parent.layoutManager
        if (layoutManager !is LinearLayoutManager) {
            throw IllegalArgumentException("该类只能在 {LinearLayoutManager} 中使用")
        }
        return if (layoutManager.orientation == RecyclerView.VERTICAL) Orientation.VERTICAL
        else Orientation.HORIZONTAL
    }

    /**
     * 计算偏移量
     */
    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val orientation = getOrientation(parent)
        val position = parent.getChildAdapterPosition(view)

        if (isSkip(position, parent)) {
            return
        } else {
            when (orientation) {
                Orientation.VERTICAL -> outRect.bottom = dividerHeightPx.toInt()
                Orientation.HORIZONTAL -> outRect.right = dividerHeightPx.toInt()
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val orientation = getOrientation(parent)

        (0 until parent.childCount).forEach {
            val view = parent.getChildAt(it)
            val position = parent.getChildAdapterPosition(view)

            if (isSkip(position, parent)) {
                return@forEach
            }

            when (orientation) {
                Orientation.VERTICAL -> {
                    c.drawRect(
                        startPaddingPx,
                        view.bottom.toFloat(),
                        view.right.toFloat() - endPaddingPx,
                        view.bottom.toFloat() + dividerHeightPx,
                        dividerPaint,
                    )
                }
                Orientation.HORIZONTAL -> {
                    c.drawRect(
                        view.right.toFloat(),
                        startPaddingPx,
                        view.right.toFloat() + dividerHeightPx,
                        view.bottom.toFloat() - endPaddingPx,
                        dividerPaint,
                    )
                }
            }

        }
    }

    /**
     * 是否跳过某条数据
     */
    private fun isSkip(position: Int, parent: RecyclerView): Boolean {
        val itemCount = parent.adapter?.itemCount ?: return true
        return position >= itemCount - 1
                || skipPosition?.invoke(position) == true
                || skipPosition?.invoke(position + 1) == true
    }

    private enum class Orientation {
        VERTICAL,
        HORIZONTAL,
    }
}