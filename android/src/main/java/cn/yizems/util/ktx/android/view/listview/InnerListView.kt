package cn.yizems.util.ktx.android.view.listview

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView
import cn.yizems.util.ktx.android.R
import cn.yizems.util.ktx.android.dimens.screenHeight

/**
 * 被ScrollView 包含的 ListView
 */
class InnerListView : ListView {
    /**
     * 最大高度,x%屏幕高度,不配置默认为不限制
     */
    private var maxPercentHeight = 0f

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        val ta = getContext().obtainStyledAttributes(attrs, R.styleable.InnerListView)
        maxPercentHeight = ta.getFloat(R.styleable.InnerListView_max_percent_height, -1f)
        ta.recycle()
        overScrollMode = OVER_SCROLL_NEVER
    }

    constructor(
        context: Context?, attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {
    }

    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            return
        }
        if (maxPercentHeight <= 0) {
            //右移两位 运算,因为 测量格式是 前两位 是测量模式,后面30位是 高度/宽度 (2进制)
            val expandSpec = MeasureSpec.makeMeasureSpec(
                Int.MAX_VALUE shr 2,
                MeasureSpec.AT_MOST
            )
            super.onMeasure(widthMeasureSpec, expandSpec)
        } else {
            //最大高度限定,仅限于 wrap_content模式
            val heightSize = MeasureSpec.getSize(heightMeasureSpec)
            val maxHeight = (context.screenHeight() * maxPercentHeight).toInt()
            if (maxHeight <= heightSize) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                setMeasuredDimension(
                    MeasureSpec.getSize(widthMeasureSpec),
                    Math.min(maxHeight, measuredHeight)
                )
            } else {
                val expandSpec = MeasureSpec.makeMeasureSpec(
                    Int.MAX_VALUE shr 2,
                    MeasureSpec.AT_MOST
                )
                super.onMeasure(widthMeasureSpec, expandSpec)
            }
        }
    }
}