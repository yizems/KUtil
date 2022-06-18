package cn.yizems.util.ktx.android.qq

import android.content.Context
import android.content.Intent
import android.net.Uri

/** QQ工具类 */
object QQUtil {
    /** 跳转到 QQ聊天 */
    fun toChat(context: Context, no: String): Boolean {
        try {
            val url = "mqqwpa://im/chat?chat_type=wpa&uin=$no"
            //uin是发送过去的qq号码
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            return true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            e.printStackTrace()
            return false
        }
    }

    /**
     * 在跳转到QQ群页面前，需要先获取要跳转到QQ群的Key，获取Key的网址：https://qun.qq.com/join.html
     * @param key 由官网生成的key
     */
    fun toGroup(context: Context, key: String): Boolean {
        val intent = Intent()
        intent.data =
            Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        try {
            context.startActivity(intent)
            return true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            e.printStackTrace()
            return false
        }
    }
}
