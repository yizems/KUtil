package cn.yizems.okhttp.proxy

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.webkit.ProxyConfig
import androidx.webkit.ProxyController
import androidx.webkit.WebViewFeature

@SuppressLint("StaticFieldLeak")
object OkHttpProxyManager {
    private lateinit var context: Context

    private val sp by lazy {
        context.getSharedPreferences("OkHttpProxyManager", Context.MODE_PRIVATE)
    }

    private val listeners = mutableListOf<OkHttpProxyManagerListener>()

    fun init(context: Context) {
        this.context = context
    }

    fun startConfigPage(activity: Activity) {
        activity.startActivity(Intent(activity, OkHttpProxyManagerActivity::class.java))
    }

    internal fun setProxy(host: String, port: Int, proxyWebView: Boolean) {
        sp.edit()
            .putString("host", host)
            .putInt("port", port)
            .putBoolean("proxyWebView", proxyWebView)
            .apply()
        listeners.forEach {
            it.invoke(host, port)
        }
    }

    internal fun getProxyWebView(): Boolean {
        return sp.getBoolean("proxyWebView", false)
    }

    fun getProxy(): Pair<String?, Int> {
        val host = sp.getString("host", null)
        val port = sp.getInt("port", 0)
        return host to port
    }

    internal fun clearProxy() {
        sp.edit().remove("host").remove("port").apply()
        listeners.forEach {
            it.invoke(null, 0)
        }
    }


    internal fun setWebViewProxy(host: String, port: Int) {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)) {
            val proxyConfig = ProxyConfig.Builder()
                .addProxyRule("${host}:${port}")
                .addDirect()
                .build()
            ProxyController.getInstance().setProxyOverride(proxyConfig, {
                Log.e("OkHttpProxyManager", "setProxyOverride execute")
            }, {
                Log.e("OkHttpProxyManager", "WebView代理改变");
            });
        } else {
            Log.e("OkHttpProxyManager", "WebView代理不支持");
        }
    }

    internal fun clearWebViewProxy() {
        if (WebViewFeature.isFeatureSupported(WebViewFeature.PROXY_OVERRIDE)) {
            ProxyController.getInstance().clearProxyOverride({
                Log.e("OkHttpProxyManager", "clearProxyOverride execute")
            }, {
                Log.e("OkHttpProxyManager", "WebView代理改变");
            });
        } else {
            Log.e("OkHttpProxyManager", "WebView代理不支持");
        }
    }


    fun addListener(listener: OkHttpProxyManagerListener) {
        listeners.add(listener)
        listener.invoke(getProxy().first, getProxy().second)
    }

    fun interface OkHttpProxyManagerListener {
        fun invoke(host: String?, port: Int)
    }


}
