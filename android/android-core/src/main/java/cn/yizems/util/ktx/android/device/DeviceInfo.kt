package cn.yizems.util.ktx.android.device

import android.provider.Settings
import cn.yizems.util.ktx.android.context.getGlobalContext

/**
 * 获取AndroidId
 * @see Settings.Secure.ANDROID_ID
 */
fun getAndroidId(): String {
    return Settings.System.getString(getGlobalContext().contentResolver, Settings.Secure.ANDROID_ID)
}
