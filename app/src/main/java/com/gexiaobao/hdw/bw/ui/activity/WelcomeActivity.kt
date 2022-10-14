package com.gexiaobao.hdw.bw.ui.activity

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.gexiaobao.hdw.bw.app.base.BaseActivity
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.dialog.RxUpVersionDialog
import com.gexiaobao.hdw.bw.app.ext.countDownCoroutines
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.ActivityWelcomeBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.MainViewModel
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.coroutines.Job
import me.hgj.mvvmhelper.base.appContext
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import java.io.File

/**
 * created by : huxiaowei
 * @date : 2022/09/06
 * Describe : 欢迎页
 */
class WelcomeActivity : BaseActivity<MainViewModel, ActivityWelcomeBinding>() {

    private lateinit var rxDialog: RxUpVersionDialog
    private var isLogin = false
    private var isIntiFirst = false
    private var job: Job? = null
    private var permissionExplainUrl = ""
    private var privacyAgreementBreviaryUrl = ""
    private var privacyAgreementUrl = ""
    private var registerAgreementUrl = ""
    val URLStr = "https://cdn.weizhiyou.top/down/wzy.apk" //apk地址
    private var progressNum = 0


    override fun initView(savedInstanceState: Bundle?) {
        isIntiFirst = CacheUtil.isInitFirst()
        rxDialog = RxUpVersionDialog(this)
        initData()
//        fcmTokenUpRequest()
        fetchAppVersion()
    }

    private fun fetchAppVersion() {
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.fetchAppVersion(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {
                showNewVersionDialog()
//                getPrivacyAgreement()
//                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
            }
        }
    }

    private fun showNewVersionDialog() {
        rxDialog.btnUpdateNow.setOnClickListener {
            downloadApk()
        }
        rxDialog.setProgressBar(progressNum)
        rxDialog.ivClose.setOnClickListener {
            rxDialog.dismiss()
            startActivity<MainActivity>()
            finish()
        }
        rxDialog.setFullScreenWidth()
        rxDialog.show()
    }

    private fun downloadApk() {
        DownloadUtil.get().download(URLStr, "download", object : DownloadUtil.OnDownloadListener {
            override fun onDownloadSuccess(str: File?) {

            }

            override fun onDownloading(progress: Int) {
                progressNum = progress
                LogUtils.debugInfo("-------" + progressNum)
            }

            override fun onDownloadFailed() {

            }

        })
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
        getDeviceId()
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
                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                if (data != null) {
                    permissionExplainUrl =
                        data.getString("0613041B1F05051F1918330E061A171F1823041A")
                    registerAgreementUrl =
                        data.getString("0413111F0502130437110413131B13180223041A")
                    privacyAgreementUrl = data.getString("06041F0017150F37110413131B13180223041A")
                    privacyAgreementBreviaryUrl =
                        data.getString("06041F0017150F37110413131B131802340413001F17040F23041A")
                }
                mBind.ivWelcome.postDelayed({
                    if (isIntiFirst) {
                        startActivity<PrivacyAgreementActivity>(
                            "permissionExplainUrl" to permissionExplainUrl,
                            "registerAgreementUrl" to registerAgreementUrl,
                            "privacyAgreementUrl" to privacyAgreementUrl,
                            "privacyAgreementBreviaryUrl" to privacyAgreementBreviaryUrl
                        )
                    } else {
                        startActivity<MainActivity>()
                    }
                    finish()
                }, 800)

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
//            if (isIntiFirst) {
//                startActivity<PrivacyAgreementActivity>()
//            } else {
//                startActivity<MainActivity>()
//            }
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

