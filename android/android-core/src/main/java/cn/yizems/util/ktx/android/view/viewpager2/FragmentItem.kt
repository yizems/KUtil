package cn.yizems.util.ktx.android.view.viewpager2

import androidx.fragment.app.Fragment

/**
 * fragment item
 * 配合 [SafePagerAdapter] 使用
 *
 * warn: 不要使用匿名内部类!!!不要使用匿名内部类!!!不要使用匿名内部类!!!
 */


interface IFragmentItem {
    fun create(position: Int): Fragment
}

interface IconFragmentItem : IFragmentItem {
    val selIcon: Int
    val defaultIcon: Int
}

/** 标题 , title 为 字符串 id */
interface TitleResFragmentItem : IFragmentItem {
    val title: Int
}

interface TitleFragmentItem : IFragmentItem {
    val title: String
}
