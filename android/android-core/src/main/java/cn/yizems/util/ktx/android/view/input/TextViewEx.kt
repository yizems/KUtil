package cn.yizems.util.ktx.android.view.input

import android.text.InputFilter
import android.widget.EditText
import android.widget.TextView
import cn.yizems.util.ktx.comm.type.toDoubleEx
import cn.yizems.util.ktx.comm.type.toDoubleOrElse
import cn.yizems.util.ktx.comm.type.toIntOrElse

/**
 * 适用于TextView 和EditText的扩展
 */


/** 是否为空, 空白字符也认为是空 */
fun TextView.isEmpty(): Boolean {
    return this.text.trim().isEmpty()
}

/** 非空 */
fun TextView.isNotEmpty(): Boolean {
    return !isEmpty()
}

/** trim 过的文本 */
fun TextView.getTrimmedText(): String {
    return this.text.trim().toString()
}

/** 设置最大输入长度 */
fun TextView.setMaxLengths(max: Int) {
    filters = this.filters.filterNot { it is InputFilter.LengthFilter }
        .toMutableList()
        .apply {
            add(InputFilter.LengthFilter(max))
        }.toTypedArray()
}

/** 获取double值 */
fun TextView.getDouble(): Double? {
    return getTrimmedText().toDoubleEx()
}

/** 获取double值, 如果转换失败或为空, 则返回 [default] */
fun TextView.getDoubleOrElse(default: Double = 0.0): Double {
    return getTrimmedText().toDoubleOrElse(default)
}

/** 获取 int 值 */
fun TextView.getInt(): Int {
    return getTrimmedText().toInt()
}

/** 获取int值, 如果转换失败或为空, 则返回 [default] */
fun TextView.getIntOrElse(default: Int = 0): Int {
    return getTrimmedText().toIntOrElse(default)
}

/**  为空或者只有`Blank`时返回默认值: [default] 不可为null */
fun TextView.getOrElse(default: String): String {
    if (this.text.trim().isEmpty()) {
        return default
    }
    return this.text.trim().toString()
}

/**  为空或者只有`Blank`时返回默认值: [default] 可为 null */
fun TextView.getOrElseNullable(default: String?): String? {
    if (this.text.trim().isEmpty()) {
        return default
    }
    return this.text.trim().toString()
}


fun TextView.clear() {
    text = ""
}

fun EditText.clear() {
    setText("")
}

/**
 * 是否只读
 */
var EditText.readonly: Boolean
    get() = !this.editable
    set(value) {
        editable = !value
    }

/**
 * 是否可以编辑
 */
internal var EditText.editable: Boolean
    set(value) {
        isEnabled = value
        isFocusable = value
        isFocusableInTouchMode = value
        isLongClickable = value
    }
    get() = this.isEnabled && this.isFocusable && this.isFocusableInTouchMode

/**
 * 字符转大写
 * @receiver TextView
 */
fun TextView.allCaps() {
    val filters = this.filters.toMutableList()
    filters.add(InputFilter.AllCaps())
    this.filters = filters.toTypedArray()
}

/**
 * 取消字符转大写
 * @receiver TextView
 */
fun TextView.removeAllCaps() {
    this.filters = this.filters
        .filterNot { it is InputFilter.AllCaps }
        .toTypedArray()
}

/**
 * 数据变化了的监听
 */
fun TextView.setOnTextChangedListener(onTextChanged: () -> Unit) {
    addTextChangedListener(SimpleTextWatcher(onTextChanged))
}
