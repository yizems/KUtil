package cn.yizems.util.ktx.android.view.input

import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import cn.yizems.util.ktx.comm.type.toDoubleEx
import cn.yizems.util.ktx.comm.type.toDoubleOrElse
import cn.yizems.util.ktx.comm.type.toIntOrElse

/**
 * 适用于TextView 和EditText的扩展
 */
fun TextView.isEmpty(): Boolean {
    return this.text.trim().isEmpty()
}

fun TextView.isNotEmpty(): Boolean {
    return !isEmpty()
}

fun TextView.getTextByTrim(): String {
    return this.text.trim().toString()
}

fun TextView.getTextByTrimOrDefault(default: String): String {
    return this.text.trim().toString().orEmpty().orDefault(default)
}

fun TextView.setMaxLengths(max: Int) {
    filters = this.filters.filterNot { it is InputFilter.LengthFilter }
        .toMutableList()
        .apply {
            add(InputFilter.LengthFilter(max))
        }.toTypedArray()
}

fun TextView.getDouble(): Double? {
    return getTextByTrim().toDoubleEx()
}

fun TextView.getDoubleOrElse(default: Double = 0.0): Double {
    return getTextByTrim().toDoubleOrElse(default)
}

fun TextView.getInt(): Int {
    return getTextByTrim().toInt()
}

fun TextView.getIntOrElse(default: Int = 0): Int {
    return getTextByTrim().toIntOrElse(default)
}

/**
 * 为空或者只有`Blank`时返回默认值
 * @receiver TextView
 * @param default String?
 * @return String ?
 */
fun TextView.getOrElseNullable(default: String?): String? {
    if (this.text.trim().isEmpty()) {
        return default
    }
    return this.text.trim().toString()
}

fun TextView.getOrElse(default: String): String {
    if (this.text.trim().isEmpty()) {
        return default
    }
    return this.text.trim().toString()
}

fun TextView.setTextChangeListener(onTextChanged: () -> Unit): TextWatcher {
    var watcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged()
        }
    }
    this.addTextChangedListener(watcher)
    return watcher
}

fun TextView.clear() {
    text = ""
}

fun EditText.clear() {
    setText("")
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
 * 是否只读
 */
var EditText.readonly: Boolean
    get() = !this.editable
    set(value) {
        editable = !value
    }

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
