/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//from https://github.com/android/views-widgets-samples/blob/main/ViewPager2/app/src/main/java/androidx/viewpager2
package cn.yizems.util.ktx.android.viewpager2

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlin.math.absoluteValue
import kotlin.math.sign

/**
 * Layout to wrap a scrollable component inside a ViewPager2. Provided as a solution to the problem
 * where pages of ViewPager2 have nested scrollable elements that scroll in the same direction as
 * ViewPager2. The scrollable element needs to be the immediate and only child of this host layout.
 *
 * This solution has limitations when using multiple levels of nested scrollable elements
 * (e.g. a horizontal RecyclerView in a vertical RecyclerView in a horizontal ViewPager2).
 *
 * e.g.  https://github.com/android/views-widgets-samples/blob/main/ViewPager2/app/src/main/res/layout/item_nested_recyclerviews.xml
 *      //没必要在根节点, [parentViewPager] 会一直向上搜索ViewPager2 节点
 *      <androidx.viewpager2.integration.testapp.NestedScrollableHost
 *          android:layout_width="match_parent"
 *          android:layout_height="wrap_content"
 *          android:layout_marginTop="8dp">
 *          <androidx.recyclerview.widget.RecyclerView
 *              android:id="@+id/first_rv"
 *              android:layout_width="match_parent"
 *              android:layout_height="wrap_content"
 *              android:background="#FFFFFF" />
 *      </androidx.viewpager2.integration.testapp.NestedScrollableHost>
 *
 *  官方解决的是 VP2 嵌套 滑动方向一致的 子View 时,子View 无法响应触摸的问题
 *
 * @author yizeliang 修复 ViewPager2 嵌套WebView/其他 和 VP2 方向不一样时,滑动不灵敏的问题
 */

class ViewPager2NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        val parentOrientation = parentViewPager?.orientation
            ?: return super.onInterceptTouchEvent(e)

        // Early return if child can't scroll in same direction as parent
        if (!canChildScroll(parentOrientation, -1f) && !canChildScroll(parentOrientation, 1f)) {
            handleDifferentOrientationInterceptTouchEvent(e, parentOrientation)
        } else {
            handleSameOrientationInterceptTouchEvent(e, parentOrientation)
        }
        return super.onInterceptTouchEvent(e)
    }

    /** 处理相同方向上的事件 */
    private fun handleSameOrientationInterceptTouchEvent(e: MotionEvent, parentOrientation: Int) {

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = parentOrientation == ORIENTATION_HORIZONTAL

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept
                    parent.requestDisallowInterceptTouchEvent(false)
                } else {
                    // Gesture is parallel, query child if movement in that direction is possible
                    if (canChildScroll(parentOrientation, if (isVpHorizontal) dx else dy)) {
                        // Child can scroll, disallow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        // Child cannot scroll, allow all parents to intercept
                        parent.requestDisallowInterceptTouchEvent(false)
                    }
                }
            }
        }
    }

    /** 处理和ViewPager2 不同方向上的事件 */
    private fun handleDifferentOrientationInterceptTouchEvent(
        e: MotionEvent,
        parentOrientation: Int
    ) {

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)
        } else if (e.action == MotionEvent.ACTION_MOVE) {
            val dx = e.x - initialX
            val dy = e.y - initialY

            val isVpHorizontal = parentOrientation == ORIENTATION_HORIZONTAL

            // assuming ViewPager2 touch-slop is 2x touch-slop of child
            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                if (isVpHorizontal == (scaledDy > scaledDx)) {
                    // Gesture is perpendicular, allow all parents to intercept
                    parent.requestDisallowInterceptTouchEvent(true)
                } else {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
    }

}
