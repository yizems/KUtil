package cn.yizems.okhttp.proxy

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OkHttpProxyManagerActivity : AppCompatActivity() {

    private val etIp by lazy {
        findViewById<EditText>(R.id.et_ip)
    }

    private val etPort by lazy {
        findViewById<EditText>(R.id.et_port)
    }

    private val checkBoxWebView by lazy {
        findViewById<CheckBox>(R.id.checkbox_webview)
    }

    private val btClear by lazy {
        findViewById<Button>(R.id.bt_clear)
    }

    private val btSet by lazy {
        findViewById<Button>(R.id.bt_set)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ok_http_proxy_manager)
        OkHttpProxyManager.getProxy().let {
            etIp.setText(it.first)
            etPort.setText(it.second.toString())
        }
        checkBoxWebView.isChecked = OkHttpProxyManager.getProxyWebView()

        btClear.setOnClickListener {
            OkHttpProxyManager.clearProxy()
            OkHttpProxyManager.clearWebViewProxy()
            finish()
        }

        btSet.setOnClickListener {
            if (etIp.text.toString().trim().isNullOrEmpty()) {
                Toast.makeText(this, "请输入代理地址", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (etPort.text.toString().trim().isNullOrEmpty()) {
                Toast.makeText(this, "请输入代理端口", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            OkHttpProxyManager.setProxy(
                etIp.text.toString(),
                etPort.text.toString().toInt(),
                checkBoxWebView.isChecked
            )
            if (checkBoxWebView.isChecked) {
                OkHttpProxyManager.setWebViewProxy(
                    etIp.text.toString(),
                    etPort.text.toString().toInt()
                )
            } else {
                OkHttpProxyManager.clearWebViewProxy()
            }
            finish()
        }

    }
}
