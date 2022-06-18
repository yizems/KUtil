package cn.yizems.util.ktx.android.view.input

import android.view.View
import android.widget.EditText
import cn.yizems.util.ktx.android.R


/**
 * 有焦点后 如果内容为0,删除,失去焦点,如果内容为空,自动填充0
 */
fun EditText.autoRemoveZeroOnFocused() {
    val mV = this
    this.addFocusChangeListener { _, hasFocus ->
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
    this.addFocusChangeListener(FocusMissDataChangedListener(this, onChanged))
}


private class FocusMissDataChangedListener(
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

/** 添加焦点监听事件: 可以添加多次,监听按照添加顺序调用 */
fun EditText.addFocusChangeListener(focusChangedListener: View.OnFocusChangeListener) {
    val listener = this.onFocusChangeListener
    if (listener == null) {
        this.onFocusChangeListener = focusChangedListener
        return
    }

    if (listener is FocusChangeListenerContainer) {
        listener.add(focusChangedListener)
        return
    }

    this.onFocusChangeListener = FocusChangeListenerContainer().apply {
        add(listener)
        add(focusChangedListener)
    }
    return
}

/** 移除焦点监听事件 */
fun EditText.removeFocusChangeListener(focusChangedListener: View.OnFocusChangeListener) {
    val listener = this.onFocusChangeListener ?: return

    if (listener is FocusChangeListenerContainer) {
        listener.remove(focusChangedListener)
        if (listener.isEmpty()) {
            this.onFocusChangeListener = null
        }
        return
    }

    if (listener == focusChangedListener) {
        this.onFocusChangeListener = null
        return
    }
    return
}

internal class FocusChangeListenerContainer() : View.OnFocusChangeListener {

    private val listeners = linkedSetOf<View.OnFocusChangeListener>()

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        listeners.forEach {
            it.onFocusChange(v, hasFocus)
        }
    }

    fun add(listener: View.OnFocusChangeListener) {
        listeners.add(listener)
    }

    fun remove(listener: View.OnFocusChangeListener) {
        listeners.remove(listener)
    }

    fun isEmpty() = listeners.isEmpty()

}
