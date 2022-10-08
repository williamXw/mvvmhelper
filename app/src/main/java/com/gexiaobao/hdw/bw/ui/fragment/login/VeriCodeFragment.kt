package com.gexiaobao.hdw.bw.ui.fragment.login

import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.ext.hideSoftKeyboard
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.app.util.RxTextTool.getBuilder
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentVerCodeBinding
import com.gexiaobao.hdw.bw.ui.activity.MainActivity
import com.gexiaobao.hdw.bw.ui.viewmodel.LoginViewModel
import com.gyf.immersionbar.ImmersionBar
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.net.LoadStatusEntity
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException

/**
 * created by : huxiaowei
 * @date :20220913
 * Describe : 填写验证码
 */
class VeriCodeFragment : BaseFragment<LoginViewModel, FragmentVerCodeBinding>() {

    private var mobileNum = ""
    private var noteToken = ""
    private var mAndroidID = ""
    private var mAppVersion = ""

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this)
            .keyboardEnable(true).init()
        mBind.viewmodel = mViewModel
        mViewModel.isChecked.set(true)

        arguments?.let {
            mobileNum = it.getString(Constant.MOBILE_NUMBER)!!
            noteToken = it.getString(Constant.NOTETOKEN)!!
        }
        mAndroidID = context?.let { DeviceUtil.getAndroidId(it) }.toString()
        mAppVersion = activity?.let { it1 -> DeviceUtil.getVersionCode(it1) }.toString()
    }

    override fun initData() {
        super.initData()
        initSpannableText()
        mBind.etVerCode.addTextChangedListener {
            if (it != null) {
                mViewModel.deal.set(it.toString())
                changeBtnBg(mBind.etVerCode)
            }
        }
        mBind.checkboxDeal.setOnCheckedChangeListener { _, isChecked ->
            mViewModel.isChecked.set(isChecked)
            changeBtnBg(mBind.etVerCode)
        }
    }

    private fun changeBtnBg(etVerCode: AppCompatEditText) {
        if (mViewModel.isChecked.get()) {
            if (etVerCode.length() == 4) {
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12_click)
            } else {
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
            }
        } else {
            mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
        }
    }

    private fun initSpannableText() {
        /**语音验证码点击事件*/
        val clickSpanOTP: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val rxDialogSure = RxDialogSure(context)
                rxDialogSure.setLogo(R.mipmap.icon_dialog_logo)
                rxDialogSure.setContent(
                    "Your number is being dialed, \n" +
                            "please pay attention to answer the call, and input the 4-digital OTP which you hear."
                )
                rxDialogSure.btnSure.setOnClickListener {
                    RxToast.showToast("消失了")
                    rxDialogSure.cancel()
                }
                rxDialogSure.setFullScreenWidth()
                rxDialogSure.show()
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }
        mBind.tvVoice.movementMethod = LinkMovementMethod.getInstance()
        getBuilder("").append("Can't receive OTP ?")
            .append("Try Voice OTP")
            .setClickSpan(clickSpanOTP).into(mBind.tvVoice)


        /**服务协议点击事件*/
        val clickSpanUser: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                nav().navigateAction(R.id.action_code_to_web)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }

        /**隐私政策点击事件*/
        val clickSpanPrivacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                nav().navigateAction(R.id.action_code_to_web)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }
        mBind.tvDeal.movementMethod = LinkMovementMethod.getInstance()
        getBuilder("").append("Agree")
            .append("《User Service》").setClickSpan(clickSpanUser)
            .append("&")
            .append("《Privacy Policy》").setClickSpan(clickSpanPrivacy)
            .into(mBind.tvDeal)
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnResend, mBind.btnContinueLogin) {
            when (it) {
                mBind.btnResend -> {
                    reSendVerCode()
                }
                mBind.btnContinueLogin -> {
                    loginToMain()
                }
            }
        }
    }

    private fun loginToMain() {
        val map = mapOf(
            EncryptUtil.encode("androidId") to mAndroidID,
            EncryptUtil.encode("appInstanceId") to "",
            EncryptUtil.encode("appVersion") to "1.1.0",
            EncryptUtil.encode("customerId") to 0,
            EncryptUtil.encode("customerMobile") to mobileNum,
            EncryptUtil.encode("deviceId") to mAndroidID,
            EncryptUtil.encode("deviceType") to DeviceUtil.model,
            EncryptUtil.encode("fcmToken") to "",
            EncryptUtil.encode("ip") to "",
            EncryptUtil.encode("latitude") to 0,
            EncryptUtil.encode("longitude") to 0,
            EncryptUtil.encode("marketId") to 10033,
            EncryptUtil.encode("noteToken") to noteToken,
//            EncryptUtil.encode("otpCode") to mViewModel.deal.get(),
            EncryptUtil.encode("otpCode") to 123456,
            EncryptUtil.encode("regGaid") to "",
        )
        val params = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody =
            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(EncryptUtil.encryptBody(params)).toString())
        when {
            mViewModel.deal.get().length != 4 -> showDialogMessage("Enter your phone OTP")
            !mBind.checkboxDeal.isChecked -> showDialogMessage("Please selected...")
            else -> {
                mViewModel.loginCallBack(paramsBody)?.observe(this) {
                    if (it.code == 200) {
                        parseLoginData(it)
                    }
                }
            }
        }
    }

    private fun reSendVerCode() {
        val map = mapOf(
            EncryptUtil.encode("androidId") to mAndroidID,
            EncryptUtil.encode("appVersion") to mAppVersion,
            EncryptUtil.encode("customerAge") to "",
            EncryptUtil.encode("customerId") to 0,
            EncryptUtil.encode("customerMobile") to mViewModel.mobileNum.get(),
            EncryptUtil.encode("customerName") to "",
            EncryptUtil.encode("customerVa1") to "",
            EncryptUtil.encode("gaid") to "",
            EncryptUtil.encode("isVoice") to true,
            EncryptUtil.encode("marketId") to 10033
        )

        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody =
            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(EncryptUtil.encryptBody(parmas)).toString())
        mViewModel.customerOtpCallBack(paramsBody)?.observe(this) {
            hideSoftKeyboard(activity)
            parseData(it)
        }
    }

    private fun parseLoginData(it: Response?) {
        var code = -1
        var msg = ""
        var mResponse = ""
        if (it!!.code == 200) {
            val dataBody = JSONObject(it.body!!.string())
            val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
            val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
            var thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
            mResponse = EncryptUtil.decode(thirdKey)
            Log.i("-------------->>>", mResponse)
            msg = JSONObject(mResponse).getString(RxConstants.MSG)
            code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
        } else {
            if (it != null) {
                LogUtils.debugInfo(it.code.toString() + it.message)
            }
        }
        hideSoftKeyboard(activity)
        if (code == NetUrl.SUCCESS_CODE) {
            val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
            val ll = data.getBoolean("11")
            val f = data.getInt("1F12")
            val a = data.getBoolean("141A17151D35030502191B1304")
            startActivity<MainActivity>()
            activity?.finish()
        } else {
            showDialogMessage(msg)
            if (code != NetUrl.SUCCESS_CODE) {
                throw ParseException(code.toString(), msg, it)
            }
        }
    }


    private fun parseData(it: Response?) {
        var code = -1
        var msg = ""
        var token = ""
        var mResponse = ""
        if (it!!.code == 200) {
            val dataBody = JSONObject(it.body!!.string())
            val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
            val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
            var thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
            mResponse = EncryptUtil.decode(thirdKey)
            msg = JSONObject(mResponse).getString(RxConstants.MSG)
            code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
        } else {
            if (it != null) {
                LogUtils.debugInfo(it.code.toString() + it.message)
            }
        }
        hideSoftKeyboard(activity)
        if (code == NetUrl.SUCCESS_CODE) {
            val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
            token = JSONObject(data.toString()).getString("1819021322191D1318")
            noteToken = token
        } else {
            showDialogMessage(msg)
            if (code != NetUrl.SUCCESS_CODE) {
                throw ParseException(code.toString(), msg, it)
            }
        }

    }

    override fun onRequestSuccess() {
        super.onRequestSuccess()
        mViewModel.loginResult.observe(this, Observer {
            //请求成功  可以做保存信息等操作 ....
            LiveDataEvent.loginEvent.value = true //通知登录成功
        })
    }

    override fun onRequestError(loadStatus: LoadStatusEntity) {
        super.onRequestError(loadStatus)
        when (loadStatus.requestCode) {
            NetUrl.CUSTOMER_OTP, NetUrl.LOGIN -> {
//                showDialogMessage(loadStatus.errorMessage)
            }
        }
    }

}