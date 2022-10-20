package com.gexiaobao.hdw.bw.data.downloadmanager

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.core.content.FileProvider
import com.gexiaobao.hdw.bw.app.util.SPUtils
import me.hgj.mvvmhelper.base.appContext
import java.io.File
import java.text.DecimalFormat
import java.util.*


/**
 * @ClassName: DownLoadMangerUtils
 * @Description: java类作用描述
 * @Author: zengqiang
 * @github: https://github.com/zqMyself
 * @Date: 2021-07-09 14:44
 */
class DownloadManagerUtils {

    var downloadManager: DownloadManager? = null
    var id = 0L
    var url = ""
    var mHandler = Handler(Looper.getMainLooper())
    var timer: Timer? = null
    var directory = ""
    lateinit var file: File

    companion object {
        private var listenerList = ArrayList<DownLoadApkListener>()
        fun setDownLoadApkListener(downLoadApkListener: DownLoadApkListener) {
            listenerList.add(downLoadApkListener)
        }

        fun removeDownLoadApkListener(downLoadApkListener: DownLoadApkListener) {
            listenerList.remove(downLoadApkListener)
        }

        fun downLoad(title: String, description: String, downLoadHtmlUrl: String) {

        }

        fun getDataSize(size: Long): String? {
            var GB = 1024 * 1024 * 1024;//定义GB的计算常量
            var MB = 1024 * 1024;//定义MB的计算常量
            var KB = 1024;//定义KB的计算常量
            var df = DecimalFormat("0.00");//格式化小数
            var resultSize = "";
            if (size / GB >= 1) {
                //如果当前Byte的值大于等于1GB
                resultSize = df.format(size / GB.toFloat()) + "GB";
            } else if (size / MB >= 1) {
                //如果当前Byte的值大于等于1MB
                resultSize = df.format(size / MB.toFloat()) + "MB";
            } else if (size / KB >= 1) {
                //如果当前Byte的值大于等于1KB
                resultSize = df.format(size / KB.toFloat()) + "KB";
            } else {
                resultSize = "$size B";
            }
            return resultSize;
        }
    }


    private fun notifyListener(state: Int, progress: Int) {
        mHandler.post {
            var iterator = listenerList.iterator()
            while (iterator.hasNext()) {
                var listener = iterator.next()
                listener.call(url, state, progress)
            }
        }

    }

    fun updateDownLoad(downLoadHtmlUrl: String) {
        val apkFile: File = checkLocalUpdate("Taurus") as File
        if (apkFile != null) {
            // 本地存在新版本，直接安装
            installApk(apkFile)
            return
        }

        val index = downLoadHtmlUrl.lastIndexOf("/")
        val apkName: String = downLoadHtmlUrl.substring(index + 1, downLoadHtmlUrl.length)
        this.url = downLoadHtmlUrl
        var uri = Uri.parse(downLoadHtmlUrl)
        val request = DownloadManager.Request(uri)
        //设置漫游条件下是否可以下载
        request.setAllowedOverRoaming(false)
        // 自定义的下载目录,注意这是涉及到android Q的存储权限，建议不要用getExternalStorageDirectory（）
        request.setDestinationInExternalFilesDir(appContext, "Taurus", appContext.packageName + ".apk")
        deleteApkFile(
            Objects.requireNonNull<File>(
                appContext.getExternalFilesDir(
                    "Taurus" + File.separator + appContext.packageName + ".apk"
                )
            )
        )
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        //设置通知标题
//        request.setTitle(title)
        //设置通知标题message
//        request.setDescription(description)
        request.setVisibleInDownloadsUi(true)
        request.allowScanningByMediaScanner()
        //设置文件存放路径
        directory = getDiskCacheDir(appContext)
        file = File(directory, "/download/$apkName")
        request.setDestinationUri(Uri.fromFile(file))

        if (downloadManager == null)
            downloadManager = appContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            var id = downloadManager!!.enqueue(request)
            this.id = id
            if (SPUtils.spUtils.contains(url)) {
                checkStatus(id)
            } else {
                SPUtils.spUtils.put(url, id)
                startTimer()
            }

        }

    }


    private fun startTimer() {
        if (timer == null) {
            timer = Timer()
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    checkStatus(id)
                }
            }, 100, 100)
        }
    }

    private fun getDiskCacheDir(context: Context): String {
        var cachePath: String? = null
        cachePath =
            if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
                context.externalCacheDir!!.path
            } else {
                context.cacheDir.path
            }
        return cachePath
    }

    @SuppressLint("Range")
    private fun checkStatus(id: Long) {
        val query = DownloadManager.Query()

        //通过下载的id查找
        query.setFilterById(id)
        var cursor: Cursor? = null
        cursor = downloadManager!!.query(query)
        if (cursor == null) {
            notifyListener(DownloadManager.STATUS_FAILED, 100)
            if (timer != null)
                timer!!.cancel()
            SPUtils.spUtils.remove(url)
            return
        }
        if (cursor.moveToFirst()) {
            val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))

            when (status) {
                DownloadManager.STATUS_PAUSED -> {
                    Log.e("tag", "STATUS_PAUSED +" + DownloadManager.STATUS_PAUSED)
                }
                DownloadManager.STATUS_PENDING -> {
                    Log.e("tag", "STATUS_PENDING +" + DownloadManager.STATUS_PENDING)
                    startTimer()
                }
                DownloadManager.STATUS_RUNNING -> {
                    var bytesAndStatus =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
                    var totalSize =
                        cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))
                    var dl_progress = ((bytesAndStatus * 100L) / totalSize)
                    notifyListener(DownloadManager.STATUS_RUNNING, dl_progress.toInt())
                    Log.e("tag", "STATUS_PENDING  bytesAndStatus = $dl_progress")
                }
                DownloadManager.STATUS_SUCCESSFUL -> {
                    notifyListener(DownloadManager.STATUS_SUCCESSFUL, 100)
                    var name = ""
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        var fileUriIdx = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                        var fileUri = cursor.getString(fileUriIdx)
                        if (fileUri != null) {
                            name = Uri.parse(fileUri).path!!
                        }
                    } else {
                        name = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME))
                    }
                    if (timer != null) {
                        timer!!.cancel()
                    }
                    //下载完成
                    cursor.close()
                    SPUtils.spUtils.remove(url)
                    Log.e("tag", "STATUS_SUCCESSFUL +" + DownloadManager.STATUS_SUCCESSFUL + ",id=" + id)
                    installApk(File(name))
                }
                DownloadManager.STATUS_FAILED -> {
                    Log.e("tag", "STATUS_FAILED +" + DownloadManager.STATUS_FAILED)
                    if (timer != null) {
                        timer!!.cancel()
                    }
                    cursor.close()
                }

            }
        }
    }

    private fun installApk(file: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true) //表明不是未知来源
            val uri = FileProvider.getUriForFile(appContext, appContext.packageName + ".fileprovider", file)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            appContext.startActivity(intent)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive")
            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true) //表明不是未知来源
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            appContext.startActivity(intent)
        }
    }

    /**
     * 下载前清空本地缓存的文件
     */
    private fun deleteApkFile(destFileDir: File) {
        if (!destFileDir.exists()) {
            return
        }
        if (destFileDir.isDirectory) {
            val files = destFileDir.listFiles()
            if (files != null) {
                for (f in files) {
                    deleteApkFile(f)
                }
            }
        }
        destFileDir.delete()
    }

    /**
     * 检查本地是否有已经下载的最新apk文件
     *
     * @param filePath 文件相对路劲
     */
    private fun checkLocalUpdate(filePath: String): File? {
        try {
            val apkFile: File?
            apkFile = if (TextUtils.isEmpty(filePath)) {
                appContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS + File.separator + appContext.packageName + ".apk")
            } else {
                appContext.getExternalFilesDir(filePath + File.separator + appContext.packageName + ".apk")
            }
            // 注意系统的getExternalFilesDir（）方法如果找不到文件会默认当成目录创建
            if (apkFile != null && apkFile.isFile) {
                val packageManager = appContext.packageManager
                val packageInfo = packageManager.getPackageArchiveInfo(apkFile.absolutePath, PackageManager.GET_ACTIVITIES)
                if (packageInfo != null) {
                    val apkVersionCode =
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
                    if (apkVersionCode > getAppCode()) {
                        return apkFile
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("----", "checkLocalUpdate:本地目录没有已经下载的新版本")
        }
        return null
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用版本号
     */
    private fun getAppCode(): Long {
        try {
            //获取包管理器
            val pm = appContext.packageManager
            //获取包信息
            val packageInfo = pm.getPackageInfo(appContext.packageName, 0)
            //返回版本号
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) packageInfo.longVersionCode else packageInfo.versionCode.toLong()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return 0
    }

}