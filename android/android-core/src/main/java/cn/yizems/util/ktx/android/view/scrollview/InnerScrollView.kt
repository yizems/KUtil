package cn.yizems.util.ktx.android.view.scrollview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView
import cn.yizems.util.ktx.android.R
import cn.yizems.util.ktx.android.screen.getScreenSize

/**
 * 可以被嵌套的ScrollView,实现原理和 [cn.yizems.util.ktx.android.view.listview.InnerListView] 一样
 */
class InnerScrollView : ScrollView {
    private var maxHeightPercent = 0f

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.InnerScrollView)
        maxHeightPercent = ta.getFloat(R.styleable.InnerScrollView_max_percent_height, -1f)
        ta.recycle()
        overScrollMode = View.OVER_SCROLL_NEVER
    }

    protected override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (maxHeightPercent <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        } else {
            //最大高度限定,仅限于 wrap_content模式
            val measuredHeight: Int = getMeasuredHeight()
            val maxHeight = (getScreenSize().y * maxHeightPercent).toInt()
            if (maxHeight <= measuredHeight) {
                //最大高度限定
                val expandSpec: Int = MeasureSpec.makeMeasureSpec(
                    maxHeight,
                    MeasureSpec.EXACTLY
                )
                super.onMeasure(widthMeasureSpec, expandSpec)
            } else {
                val expandSpec: Int = MeasureSpec.makeMeasureSpec(
                    Int.MAX_VALUE shr 2,
                    MeasureSpec.AT_MOST
                )
                super.onMeasure(widthMeasureSpec, expandSpec)
            }
        }
    }
}
