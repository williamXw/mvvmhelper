package com.gexiaobao.hdw.bw.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import com.gexiaobao.hdw.bw.app.base.BaseActivity
import com.gexiaobao.hdw.bw.app.dialog.RxUpVersionDialog
import com.gexiaobao.hdw.bw.app.ext.countDownCoroutines
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.data.downloadmanager.DownloadManagerUtils
import com.gexiaobao.hdw.bw.databinding.ActivityWelcomeBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.MainViewModel
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.coroutines.Job
import me.hgj.mvvmhelper.base.appContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.jetbrains.anko.startActivity
import org.json.JSONObject
import java.util.*
import kotlin.system.exitProcess

/**
 * created by : huxiaowei
 * @date : 2022/09/06
 * Describe : 欢迎页
 */
@RequiresApi(Build.VERSION_CODES.M)
class WelcomeActivity : BaseActivity<MainViewModel, ActivityWelcomeBinding>() {

    private lateinit var rxDialog: RxUpVersionDialog
    private var isLogin = false
    private var isIntiFirst = false
    private var job: Job? = null
    private var permissionExplainUrl = ""
    private var privacyAgreementBreviaryUrl = ""
    private var privacyAgreementUrl = ""
    private var registerAgreementUrl = ""
    private var progressNum = 0
    private var isNeedUpDate = false

    /**
     * 8.0未知应用授权请求码
     */
    private val INSTALL_PACKAGES_REQUESTCODE = 1112

    /**
     * 用户跳转未知应用安装的界面请求码
     */
    private val GET_UNKNOWN_APP_SOURCES = 1113


    override fun initView(savedInstanceState: Bundle?) {
        isIntiFirst = CacheUtil.isInitFirst()
        rxDialog = RxUpVersionDialog(this)
        initData()
        fcmTokenUpRequest()
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
//                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                showNewVersionDialog()
//                getPrivacyAgreement()//判断是否有更新   有 去更新  无  请求获取权限连接接口
            }
        }
    }

    private fun showNewVersionDialog() {
        rxDialog.setProgressBar(progressNum)
        rxDialog.btnUpdateNow.setOnClickListener {
            requestPermission()
            rxDialog.llUpdate.visibility = View.GONE
            rxDialog.llProgress.visibility = View.VISIBLE
        }
        rxDialog.ivClose.setOnClickListener {
            rxDialog.dismiss()
            startActivity<MainActivity>()
            finish()
        }
        rxDialog.setCanceledOnTouchOutside(false)
        rxDialog.setFullScreenWidth()
        rxDialog.show()
    }

    private fun requestPermission() {
        //权限判断是否有访问外部存储空间权限
        //权限判断是否有访问外部存储空间权限
        val flag = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (flag != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 用户拒绝过这个权限了，应该提示用户，为什么需要这个权限。
                RxToast.showToast("请授权访问存储空间权限")
            }
            // 申请授权
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            DownloadManagerUtils().updateDownLoad("https://downpack.baidu.com/baidumap_AndroidPhone_1012337a.apk")
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //8.0应用设置界面未知安装开源返回时候
        if (requestCode == GET_UNKNOWN_APP_SOURCES && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val allowInstall: Boolean = Objects.requireNonNull(this).packageManager.canRequestPackageInstalls()
            if (allowInstall) {
                rxDialog.dismiss()
            } else {
                exitProcess(0)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //6.0 存储权限授权结果回调
                DownloadManagerUtils().updateDownLoad("https://downpack.baidu.com/baidumap_AndroidPhone_1012337a.apk")
            } else {
                rxDialog.dismiss()
                exitProcess(0)
            }
        } else if (requestCode == INSTALL_PACKAGES_REQUESTCODE) {
            // 8.0的权限请求结果回调,授权成功
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                DownloadManagerUtils().updateDownLoad("https://downpack.baidu.com/baidumap_AndroidPhone_1012337a.apk")
            } else {
                // 授权失败，引导用户去未知应用安装的界面
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    //注意这个是8.0新API
                    val packageUri = Uri.parse("package:" + this.packageName)
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri)
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (job != null) {
            job?.cancel()
            job = null
        }
    }
}

