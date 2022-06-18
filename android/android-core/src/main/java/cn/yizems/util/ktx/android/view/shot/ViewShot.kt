package cn.yizems.util.ktx.android.view.shot

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.util.LruCache
import android.view.View
import android.widget.ListView
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * View 截图
 *
 * 如果是已经绘制的View, 可以先尝试下 [androidx.core.view.drawToBitmap]
 */
object ViewShot {

    /**
     * 手动测量摆放View
     * 对于手动 inflate 或者其他方式代码生成加载的View进行测量，避免该View无尺寸
     * @param v
     * @param width
     * @param height
     */
    fun layoutView(v: View, width: Int, height: Int) {
        // validate view.width and view.height
        v.layout(0, 0, width, height)
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)

        // validate view.measurewidth and view.measureheight
        v.measure(measuredWidth, measuredHeight)
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight())
    }


    /**
     * 获取一个 View 的缓存视图
     * (前提是这个View已经渲染完成显示在页面上)
     * @param view
     * @return
     */
    fun getCacheBitmapFromView(view: View): Bitmap? {
        val drawingCacheEnabled = true
        view.setDrawingCacheEnabled(drawingCacheEnabled)
        view.buildDrawingCache(drawingCacheEnabled)
        val drawingCache = view.getDrawingCache()
        val bitmap: Bitmap?
        if (drawingCache != null) {
            bitmap = Bitmap.createBitmap(drawingCache!!)
            view.setDrawingCacheEnabled(false)
        } else {
            bitmap = null
        }
        return bitmap
    }

    /**
     * 对ScrollView进行截图
     * @param scrollView
     * @return
     */
    fun shotScrollView(scrollView: ScrollView): Bitmap? {
        var h = 0
        var bitmap: Bitmap? = null
        for (i in 0 until scrollView.childCount) {
            h += scrollView.getChildAt(i).height
//            scrollView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"))
        }
        bitmap = Bitmap.createBitmap(scrollView.width, h, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        scrollView.draw(canvas)
        return bitmap
    }


    /**
     * 对ListView进行截图
     * http://stackoverflow.com/questions/12742343/android-get-screenshot-of-all-listview-items
     */
    fun shotListView(listview: ListView): Bitmap {

        val adapter = listview.getAdapter()
        val itemscount = adapter.getCount()
        var allitemsheight = 0
        val bmps = ArrayList<Bitmap>()

        for (i in 0 until itemscount) {

            val childView = adapter.getView(i, null, listview)
            childView.measure(
                View.MeasureSpec.makeMeasureSpec(listview.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )

            childView.layout(0, 0, childView.getMeasuredWidth(), childView.getMeasuredHeight())
            childView.setDrawingCacheEnabled(true)
            childView.buildDrawingCache()
            bmps.add(childView.getDrawingCache())
            allitemsheight += childView.getMeasuredHeight()
        }

        val bigbitmap = Bitmap.createBitmap(
            listview.getMeasuredWidth(),
            allitemsheight,
            Bitmap.Config.ARGB_8888
        )
        val bigcanvas = Canvas(bigbitmap)

        val paint = Paint()
        var iHeight = 0

        for (i in bmps.indices) {
            val bmp: Bitmap = bmps[i]
            bigcanvas.drawBitmap(bmp, 0F, iHeight.toFloat(), paint)
            iHeight += bmp.height

            bmp.recycle()
        }

        return bigbitmap
    }


    /**
     * 对RecyclerView进行截图
     * https://gist.github.com/PrashamTrivedi/809d2541776c8c141d9a
     */
    fun shotRecyclerView(view: RecyclerView): Bitmap? {
        val adapter = view.adapter
        var bigBitmap: Bitmap? = null
        if (adapter != null) {
            val size = adapter.itemCount
            var height = 0
            val paint = Paint()
            var iHeight = 0
            val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

            // Use 1/8th of the available memory for this memory cache.
            val cacheSize = maxMemory / 8
            val bitmaCache = LruCache<String, Bitmap>(cacheSize)
            for (i in 0 until size) {
                val holder = adapter.createViewHolder(view, adapter.getItemViewType(i))
                adapter.onBindViewHolder(holder, i)
                holder.itemView.measure(
                    View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                holder.itemView.layout(
                    0, 0, holder.itemView.measuredWidth,
                    holder.itemView.measuredHeight
                )
                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val drawingCache = holder.itemView.drawingCache
                if (drawingCache != null) {

                    bitmaCache.put(i.toString(), drawingCache)
                }
                height += holder.itemView.measuredHeight
            }

            bigBitmap = Bitmap.createBitmap(view.measuredWidth, height, Bitmap.Config.ARGB_8888)
            val bigCanvas = Canvas(bigBitmap)
            val lBackground = view.background
            if (lBackground is ColorDrawable) {
                val lColor = lBackground.color
                bigCanvas.drawColor(lColor)
            }

            for (i in 0 until size) {
                val bitmap = bitmaCache.get(i.toString())
                bigCanvas.drawBitmap(bitmap, 0F, iHeight.toFloat(), paint)
                iHeight += bitmap!!.height
                bitmap.recycle()
            }
        }
        return bigBitmap
    }
}
