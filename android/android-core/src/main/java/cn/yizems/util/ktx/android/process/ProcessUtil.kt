package cn.yizems.util.ktx.android.process

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
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

    /** 获取当前进程名字 */
    @SuppressLint("PrivateApi", "DiscouragedPrivateApi", "ObsoleteSdkInt")
    @JvmStatic
    fun getCurrentProcessName(context: Context): String? {
        if (!cacheProcessName.isNullOrBlank()) {
            return cacheProcessName
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            cacheProcessName = Application.getProcessName()
            return cacheProcessName
        }
        // api 18 之后, 可以通过 ActivityManager.getMyMemoryState 获取进程名字
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            try {
                val activityThreadClazz = Class.forName("android.app.ActivityThread")
                val currentProcessMethod =
                    activityThreadClazz.getDeclaredMethod("currentProcessName")
                currentProcessMethod.isAccessible = true
                val result = currentProcessMethod.invoke(null) as String
                if (result.isNotBlank()) {
                    cacheProcessName = result
                    return cacheProcessName
                }
            } catch (thr: Throwable) {

            }
        }


        val pid = Process.myPid()
        val uid = Process.myUid()
        val mActivityManager = context
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in mActivityManager.runningAppProcesses) {
            if (appProcess.pid == pid && appProcess.uid == uid) {
                cacheProcessName = appProcess.processName
                return cacheProcessName
            }
        }
        return null
    }

    /** 当前是否在主进程 */
    @JvmStatic
    fun isMainProcess(context: Context): Boolean {
        return getCurrentProcessName(context) == context.packageName
    }

}

///** 是否主进程 */
//fun Context.isMainProcess(): Boolean {
//    return ProcessUtil.isMainProcess(this)
//}
//
///** 获取当前进程名字 */
//fun Context.getCurrentProcessName(): String? {
//    return ProcessUtil.getCurrentProcessName(this)
//}
