package cn.yizems.util.ktx.android.view.input

import android.text.Editable
import android.text.TextWatcher

/**
 * 简单的TextChangeListener
 * Created by YZL on 2017/12/20.
 */
class KtTextWatcher(var onTextChanged: () -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    private var handing = false

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (handing) {
            return
        }
        handing = true
        onTextChanged()
        handing = false
    }

}