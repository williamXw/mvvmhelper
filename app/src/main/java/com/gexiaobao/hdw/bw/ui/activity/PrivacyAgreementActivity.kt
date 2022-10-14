package com.gexiaobao.hdw.bw.ui.activity

import android.Manifest
import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.gexiaobao.hdw.bw.app.base.BaseActivity
import com.gexiaobao.hdw.bw.app.ext.RxWebViewTool
import com.gexiaobao.hdw.bw.app.util.CacheUtil
import com.gexiaobao.hdw.bw.app.util.RxTextTool
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.app.util.startActivity
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.ActivityPrivacyAgreementBinding
import com.gexiaobao.hdw.bw.ui.dialog.BottomSheetPrivacyDialog
import com.gexiaobao.hdw.bw.ui.viewmodel.MainViewModel
import com.permissionx.guolindev.PermissionX
import com.tamsiree.rxkit.interfaces.OnWebViewLoad
import com.tencent.bugly.crashreport.CrashReport
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils

/**
 * created by : huxiaowei
 * @date : 20221013
 * Describe : 隐私协议
 */
class PrivacyAgreementActivity : BaseActivity<MainViewModel, ActivityPrivacyAgreementBinding>() {
    private var permissionExplainUrl = ""
    private var registerAgreementUrl = ""
    private var privacyAgreementUrl = ""
    private var privacyAgreementBreviaryUrl = ""

    override fun initView(savedInstanceState: Bundle?) {
        initSpannableText()
        initData()
        val isAgreePrivacy = CacheUtil.isAgreePrivacy()//首次进入 默认值 false
        if (!isAgreePrivacy) {
            showBottomDialog(registerAgreementUrl, isAgreePrivacy)
        }
    }

    private fun initData() {
        permissionExplainUrl = intent.extras?.getString("permissionExplainUrl").toString()
        privacyAgreementBreviaryUrl = intent.extras?.getString("privacyAgreementBreviaryUrl").toString()
        privacyAgreementUrl = intent.extras?.getString("privacyAgreementUrl").toString()
        registerAgreementUrl = intent.extras?.getString("registerAgreementUrl").toString()
        //设置加载进度最大值
        mBind.pbWebBase.max = 100

        RxWebViewTool.initWebView(this, mBind.webBase, object : OnWebViewLoad {
            override fun onPageStarted() {
                mBind.pbWebBase.visibility = View.VISIBLE
            }

            override fun onReceivedTitle(title: String) {}
            override fun onProgressChanged(newProgress: Int) {
                mBind.pbWebBase.progress = newProgress
            }

            override fun shouldOverrideUrlLoading() {}
            override fun onPageFinished() {
                mBind.pbWebBase.visibility = View.GONE
            }
        })
        mBind.webBase.loadUrl(permissionExplainUrl)
        LogUtils.debugInfo("帮助类完整连接", permissionExplainUrl)

        mBind.checkboxPrivacy.setOnCheckedChangeListener { _, b ->
            if (b) {
                CacheUtil.setInitFirst(false)
                permissionRequest()
            }
        }
    }

    private fun permissionRequest() {
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
            //拒绝后重新弹窗提醒
//            .onExplainRequestReason { scope, deniedList ->
//                //如果权限被拒绝多个  只有相机是必须开启才能进入程序  则需要下面的代码过滤申请
////                val filteredList = deniedList.filter {
////                    it == Manifest.permission.CAMERA
////                }
//                scope.showRequestReasonDialog(
//                    deniedList,
//                    DeviceUtil.getAppName(this) + "需要以下权限才能继续",
//                    "同意",
//                    "拒绝"
//                )
//            }.onForwardToSettings { scope, deniedList ->
//                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "好的")
//            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    CacheUtil.setPermission(true)//同意了权限 记录
                    //初始化Bugly
                    initBugly()
                } else {
                    CacheUtil.setPermission(false)//拒绝了某个权限 记录
//                    exitProcess(0)
//                    Toast.makeText(this, "您拒绝了如下权限：$deniedList", Toast.LENGTH_SHORT).show()
                }
                startActivity<MainActivity>()
                finish()
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

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnDisagree) {
            when (it) {
                mBind.btnDisagree -> {
                    finish()
                }
            }
        }
    }

    override fun onSaveInstanceState(paramBundle: Bundle) {
        super.onSaveInstanceState(paramBundle)
        paramBundle.putString("url", mBind.webBase.url)
    }

    private fun initSpannableText() {
        /**Terms点击事件*/
        val clickSpanUser: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showBottomDialog(registerAgreementUrl, true)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }

        /**PrivacyPolicy点击事件*/
        val clickSpanPrivacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                showBottomDialog(privacyAgreementUrl, true)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }
        mBind.tvPrivacy.movementMethod = LinkMovementMethod.getInstance()
        RxTextTool.getBuilder("").append("Accept")
            .append(" Terms & Conditions").setClickSpan(clickSpanUser)
            .setUnderline()
            .append(" and ")
            .append("Privacy Policy").setClickSpan(clickSpanPrivacy)
            .setUnderline()
            .append(" and to receive notification from SMS and email")
            .into(mBind.tvPrivacy)
    }

    private fun showBottomDialog(webUrl: String, isInitFirst: Boolean) {
        val dialog = BottomSheetPrivacyDialog(webUrl, isInitFirst)
        dialog.show(supportFragmentManager, "contact")
    }
}