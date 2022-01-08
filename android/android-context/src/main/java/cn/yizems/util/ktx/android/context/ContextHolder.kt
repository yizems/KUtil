@file:Suppress("NOTHING_TO_INLINE")

package cn.yizems.util.ktx.android.context

import android.annotation.SuppressLint
import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri


inline fun getGlobalContext() = ContextHolder.me()

/**
 * 获取context
 */
class ContextHolder : ContentProvider() {

    companion object {

        private var appContext: Context? = null

        fun me(): Context {
            if (appContext != null) {
                return appContext!!
            }
            appContext = getContextByReflect()
            return appContext!!
        }

        @SuppressLint("PrivateApi")
        private fun getContextByReflect(): Context {
            try {
                val activityThread = Class.forName("android.app.ActivityThread")
                val thread = activityThread.getMethod("currentActivityThread").invoke(null)
                val app = activityThread.getMethod("getApplication").invoke(thread) as? Context
                if (app != null) {
                    return app
                } else {
                    throw NullPointerException("反射获取 context 失败")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                throw NullPointerException("获取 context 异常")
            }
        }
    }

    override fun onCreate(): Boolean {
        appContext = context!!.applicationContext
        return true
    }


    //region content provider 方法


    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        throw IllegalStateException("ContextHolder can not update")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw IllegalStateException("ContextHolder can not delete")
    }

    override fun getType(uri: Uri): String? {
        throw IllegalStateException("ContextHolder can not getType")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw IllegalStateException("ContextHolder can not insert")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        throw IllegalStateException("ContextHolder can not query")
    }
    //endregion
}
