package com.gexiaobao.hdw.bw.ui.activity

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gexiaobao.hdw.bw.app.base.BaseActivity
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.ext.countDownCoroutines
import com.gexiaobao.hdw.bw.app.util.CacheUtil
import com.gexiaobao.hdw.bw.app.util.DeviceUtil
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.KvUtils
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.ActivityWelcomeBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.MainViewModel
import com.jaeger.library.StatusBarUtil
import com.permissionx.guolindev.PermissionX
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Job
import me.hgj.mvvmhelper.base.appContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import kotlin.system.exitProcess

/**
 * created by : huxiaowei
 * @date : 2022/09/06
 * Describe : 欢迎页
 */
class WelcomeActivity : BaseActivity<MainViewModel, ActivityWelcomeBinding>() {

    private var isLogin = false
    private var job: Job? = null
    private var permissionExplainUrl = ""
    private var privacyAgreementBreviaryUrl = ""
    private var privacyAgreementUrl = ""
    private var registerAgreementUrl = ""

    override fun initView(savedInstanceState: Bundle?) {
        initData()
        fcmTokenUpRequest()
    }

    private fun fcmTokenUpRequest() {
        val map = mapOf(
            EncryptUtil.encode("appInstanceId") to "",
            EncryptUtil.encode("appVersion") to DeviceUtil.getVersionCode(this),
            EncryptUtil.encode("customerId") to KvUtils.decodeInt(Constant.CUSTOMER_ID),
            EncryptUtil.encode("fcmToken") to "",
            EncryptUtil.encode("marketId") to Constant.MARKET_ID,
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.fcmTokenUp(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
        }
    }

    private fun getDeviceId() {
        isLogin = KvUtils.decodeBoolean(Constant.ISLOGIN)
        var deviceID = DeviceUtil.getAndroidId(appContext)
        KvUtils.encode(Constant.DEVICE_ID, deviceID)
    }

    private fun initData() {
        getPrivacyAgreement()
        getDeviceId()
        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CONTACTS
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            //解释申请权限的用途，不需要则不用写
//            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                //如果权限被拒绝多个  只有相机是必须开启才能进入程序  则需要下面的代码过滤申请
//                val filteredList = deniedList.filter {
//                    it == Manifest.permission.CAMERA
//                }
                scope.showRequestReasonDialog(
                    deniedList,
                    DeviceUtil.getAppName(this) + "需要以下权限才能继续",
                    "同意",
                    "拒绝"
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "好的")
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    //申请权限之后 初始化bugly
                    initBugly()
                    startCountDownCoroutines()
                } else {
                    exitProcess(0)
                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /**
     * 获取隐私协议地址
     */
    private fun getPrivacyAgreement() {
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID,
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.fetchAgreement(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {

            }
        }
    }

    private fun initBugly() {
        /**
         * 配置Bugly,根据官方提示，最好在获取权限之后初始化bugly，并且最好不要再异步线程
         * 参数3：建议在测试阶段建议设置成true，发布时设置为false
         */
        CrashReport.initCrashReport(
            applicationContext,
            Constant.APP_ID_BUGLY,
            true
        )
    }

    private fun startCountDownCoroutines() {
        val isIntiFirst = CacheUtil.isInitFirst()
        //开启倒计时3s
        job = countDownCoroutines(1, {
        }, {
            //如果登陆过就直接跳转主页面 否则去登录
            if (isIntiFirst) {
                startActivity<PrivacyAgreementActivity>()
            } else {
                startActivity<MainActivity>()
            }
            finish()
        }, lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (job != null) {
            job?.cancel()
            job = null
        }
    }
}

