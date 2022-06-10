package cn.yizems.util.ktx.android.process

import android.app.ActivityManager
import android.content.Context
import android.os.Process
import cn.yizems.util.ktx.android.process.ProcessUtil.cacheProcessName

/**
 * 进程相关工具
 * 有些莫名其妙的bug, 怀疑是多进程的问题
 * @property cacheProcessName String? 缓存进程名字
 */
object ProcessUtil {

    @Volatile
    @JvmStatic
    private var cacheProcessName: String? = null

    @JvmStatic
    fun getCurrentProcessName(context: Context): String? {
        if (!cacheProcessName.isNullOrBlank()) {
            return cacheProcessName
        }
        val pid = Process.myPid()
        val mActivityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in mActivityManager
            .runningAppProcesses) {
            if (appProcess.pid == pid) {
                cacheProcessName = appProcess.processName
                return cacheProcessName
            }
        }
        return null
    }

    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        return getCurrentProcessName(context) == context.packageName
    }

}
