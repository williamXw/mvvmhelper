package com.gexiaobao.hdw.bw.app

import android.app.Application
import com.effective.android.anchors.AnchorsManager
import com.effective.android.anchors.Project
import com.gexiaobao.hdw.bw.BuildConfig
import com.gexiaobao.hdw.bw.app.ext.mmkv
import com.gexiaobao.hdw.bw.app.util.DeviceUtil
import com.gexiaobao.hdw.bw.app.util.KvUtils
import com.tencent.bugly.crashreport.CrashReport.getDeviceID
import me.hgj.mvvmhelper.base.MvvmHelper
import me.hgj.mvvmhelper.base.appContext
import me.hgj.mvvmhelper.ext.currentProcessName

/**
 * 作者　: hegaojian
 * 时间　: 2021/6/9
 * 描述　:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MvvmHelper.init(this, BuildConfig.DEBUG)
        val processName = currentProcessName
        if (currentProcessName == packageName) {
            // 主进程初始化
            onMainProcessInit()
        } else {
            // 其他进程初始化
            processName?.let { onOtherProcessInit(it) }
        }
    }

    /**
     * @description  代码的初始化请不要放在onCreate直接操作，按照下面新建异步方法
     */
    private fun onMainProcessInit() {
        AnchorsManager.getInstance()
            .debuggable(BuildConfig.DEBUG)
            //设置锚点
            .addAnchor(InitNetWork.TASK_ID, InitUtils.TASK_ID, InitComm.TASK_ID).start(
                Project.Builder("app", AppTaskFactory())
                    .add(InitNetWork.TASK_ID)
                    .add(InitComm.TASK_ID)
                    .add(InitUtils.TASK_ID)
                    .build()
            )
    }

    /**
     * 其他进程初始化，[processName] 进程名
     */
    private fun onOtherProcessInit(processName: String) {}

}