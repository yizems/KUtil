package cn.yizems.util.ktx.android.view.input

import android.view.View
import android.widget.EditText
import cn.yizems.util.ktx.android.R


/**
 * 整数型,有焦点后 如果内容为0,删除,失去焦点,如果内容为空,自动填充0
 * @receiver EditText
 */
fun EditText.addNumbInputEvent() {
    val mV = this
    this.setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            if (mV.getOrElse("0").toDouble() == 0.0) {
                mV.setText("")
            }
        } else {
            if (mV.isEmpty()) {
                mV.setText("0")
            }
        }
    }
}

/**
 * 焦点消失时,数据变化了的监听
 */
fun EditText.setFocusMissDataChangedListener(onChanged: (oldStr: String, newStr: String) -> Unit) {
    this.onFocusChangeListener = FocusMissDataChangedListener(this, onChanged)
}

class FocusMissDataChangedListener(
    private val editText: EditText,
    val onChanged: (oldStr: String, newStr: String) -> Unit
) : View.OnFocusChangeListener {

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            editText.setTag(R.id.view_edit_focus_miss_data, editText.text.trim().toString())
        } else {
            val oldStr = editText.getTag(R.id.view_edit_focus_miss_data) as? String ?: ""
            val newStr = editText.text.trim().toString()
            if (oldStr != newStr) {
                onChanged(oldStr, newStr)
            }
        }
    }

}