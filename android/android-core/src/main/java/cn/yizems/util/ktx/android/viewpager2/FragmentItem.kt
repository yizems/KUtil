package cn.yizems.util.ktx.android.viewpager2

import androidx.fragment.app.Fragment

/**
 * fragment item
 * 配合 [SafePagerAdapter] 使用
 * 建议采用 Kotlin 密封类
 *
 * warn: 不用使用匿名内部类!!!不用使用匿名内部类!!!不用使用匿名内部类!!!
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
