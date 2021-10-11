package cn.yizems.util.ktx.android.view.input

import android.annotation.SuppressLint
import android.text.*
import android.widget.EditText
import cn.yizems.util.ktx.android.R
import kotlin.math.max
import kotlin.math.min


fun EditText.setTextSkipDecimalInputFilter(text: String) {
    this.setTag(R.id.edit_text_decimal_input_filter, true)
    setText(text)
}

/**
 * 金额输入过滤器
 * 高仿微信转账规则
 *
 * 可以精确控制整数位和小数位位置
 *
 * 配合[setTextSkipDecimalInputFilter] 可以设置进去不符合规则的数值
 *
 * 对于 负数,这里直接跳过,不做处理,负数默认为设置进去的值,而不是用户手动录入的值
 *
 * @property view EditText
 * @property prefix Int 整数位
 * @property suffix Int 小数位
 * @constructor
 */
class DecimalInputFilter(
    val view: EditText,
    val prefix: Int,
    val suffix: Int,
) : TextWatcher {

    companion object {

        /**
         *
         * @param edit EditText
         * @param pre Int 整数位
         * @param suff Int 小数位
         */
        fun attach(
            edit: EditText,
            prefix: Int = 7,
            suffix: Int = 2
        ) {

            if (suffix == 0) {
                edit.inputType = InputType.TYPE_CLASS_NUMBER
            } else {
                edit.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
            }


            edit.addTextChangedListener(DecimalInputFilter(edit, prefix, suffix))
        }
    }


    private val reg = "^\\d{0,${prefix}}(\\.\\d{0,${suffix}})?".toRegex()


    /**
     * @param s: 修改之前的文字。
     * @param start: 字符串中即将发生修改的位置。
     * @param count: 字符串中即将被修改的文字的长度。如果是新增的话则为0。
     * @param after: 被修改的文字修改之后的长度。如果是删除的话则为0。
     */
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    /**
     * @param s: 修改之后的文字。
     * @param start: 有变动的字符串的序号
     * @param before: 被改变的字符串长度，如果是新增则为0。
     * @param count: 添加的字符串长度，如果是删除则为0。
     */
    @SuppressLint("SetTextI18n")
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s ?: return

        if (view.getTag(R.id.edit_text_decimal_input_filter) == true) {
            view.setTag(R.id.edit_text_decimal_input_filter, false)
            return
        }

        if (s.startsWith("-")) {
            //负值作为异常情况, 可以认为非认为输入的,不做处理
            return
        }


        if (reg.matches(s)
            //小数点开头,需要整数位添加0
            && !s.startsWith(".")
            //不是0.开头,却以0开头,代表需要移除0
            && !(s.startsWith("0") && s.length > 1 && !s.startsWith("0."))
        ) {
            return
        }

        val handler = DecimalTextHandler(
            s.toString(),
            start,
            before,
            count,
            view.selectionStart,
            prefix,
            suffix,
        ).handle()

        view.setText(handler.newText)

        view.setSelection(max(handler.cursorIndex, 0))

    }


    private fun handleInteger(integer: String, before: Int, count: Int) {

    }


    override fun afterTextChanged(s: Editable?) {

    }
}

/**
 *
 * @property text String  修改之后的文字。
 * @property start Int  有变动的字符串的序号
 * @property before Int  被改变的字符串长度，如果是新增则为0。
 * @property count Int  添加的字符串长度，如果是删除则为0。
 * @property cursorIndex Int 光标位置
 * @constructor
 */
private class DecimalTextHandler(
    val text: String,
    val start: Int,
    val before: Int,
    val count: Int,
    var cursorIndex: Int,
    val prefix: Int,
    val suffix: Int,
) {

    var newText: String = text


    fun handle(): DecimalTextHandler {

        //小数点位置
        val pointIndex = text.indexOf(".")

        if (pointIndex >= 0) {  //包含小数点
            var integer = text.subSequence(0, pointIndex)
            var decimal = text.subSequence(pointIndex + 1, text.length)

            // 小数处理
            if (decimal.length > suffix) {
                decimal = decimal.dropLast(decimal.length - suffix)
            }

            // 整数处理

            // 删除前面多余的 0
            integer = integer.dropWhile {
                (it == '0').apply {
                    if (this) cursorIndex = max(0, cursorIndex - 1)
                }
            }

            // 空的话补 0
            if (integer.isEmpty()) {
                cursorIndex += 1
                integer = "0"
            }
            // 超出长度删除结尾
            if (integer.length > prefix) {
                integer = handleIntegerOut(integer.toString())
            }
            //如果小数位为0,移除小数点

            if (suffix == 0) {
                cursorIndex -= 1
                newText = integer.toString()
            } else {
                newText = "${integer}.$decimal"
            }

            if (cursorIndex > newText.length) {
                cursorIndex = newText.length
            }

        } else {
            //删除多余的0
            newText = text.dropWhile {
                (it == '0').apply {
                    if (this) cursorIndex = max(0, cursorIndex - 1)
                }
            }
            // 补 0
            if (text.isEmpty()) {
                newText = "0"
                cursorIndex += 1
            }
            // 超出长度裁剪
            if (text.length > prefix) {
                newText = handleIntegerOut(text)
            }

            if (cursorIndex > text.length) {
                cursorIndex = text.length
            }
        }

        return this
    }

    /**
     * 处理整数位超出后的逻辑
     * 超出后不能输入
     * @param source String
     */
    private fun handleIntegerOut(source: String): String {

        if (before > 0
            && count == 0
        ) {
            //代表是删除导致的文本变长(删除小数点),那么就应该删除后面多余的长度
            return handleIntegerOut2(source)
        }

        val outCount = source.length - prefix
        cursorIndex -= outCount
        return source.removeRange(
            min(prefix, start + count - outCount),
            min(source.length, start + count)
        )
    }

    /**
     * 整数位超出后自动往后往后覆盖顺延
     *
     * 1234 -> 输入1 ->12134->1213
     *
     * @param source String
     * @return String
     */
    private fun handleIntegerOut2(source: String): String {
        return source.substring(0, prefix)
    }

}