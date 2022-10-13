package com.gexiaobao.hdw.bw.ui.fragment.login

import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.app.util.RxTextTool.getBuilder
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.data.response.LoginInfoResponse
import com.gexiaobao.hdw.bw.databinding.FragmentLoginBinding
import com.gexiaobao.hdw.bw.ui.activity.MainActivity
import com.gexiaobao.hdw.bw.ui.viewmodel.LoginViewModel
import com.gyf.immersionbar.ImmersionBar
import com.noober.background.view.Const
import me.hgj.mvvmhelper.base.appContext
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.net.LoadStatusEntity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * created by : huxiaowei
 * @date : 20220913
 * Describe :
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

    private var noteToken = ""
    private var isToLogin = false

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).keyboardEnable(true).init()
        mBind.viewmodel = mViewModel
        mViewModel.isChecked.set(true)
        initSpannableText()
    }

    override fun showToolBar(): Boolean {
        return false
    }

    override fun initData() {
        super.initData()

        mBind.etLoginMobile.addTextChangedListener {
            if (!it.isNullOrEmpty()) {
                mViewModel.mobileNum.set(it.toString())
                changeBtnBg(mBind.etLoginMobile)
            }
        }

        mBind.etVerCode.addTextChangedListener {
            if (it != null) {
                mViewModel.deal.set(it.toString())
                changeBtnBgOpt(mBind.etVerCode)
            }
        }

        mBind.checkboxDeal.setOnCheckedChangeListener { _, isChecked ->
            mViewModel.isChecked.set(isChecked)
            changeBtnBg(mBind.etLoginMobile)
        }
    }

    private fun changeBtnBg(it: AppCompatEditText) {
        if (it.text?.startsWith("0") == true) {
            if (it.length() == 11 && mViewModel.isChecked.get())
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12_click)
            else
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
        } else {
            if (it.length() == 10 && mViewModel.isChecked.get())
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12_click)
            else
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
        }
    }

    private fun changeBtnBgOpt(etVerCode: AppCompatEditText) {
        if (mViewModel.isChecked.get()) {
            if (etVerCode.length() == 6) {
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12_click)
            } else {
                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
            }
        } else {
            mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
        }
    }

    private fun initSpannableText() {
        /**用户协议点击事件*/
        val clickSpanUser: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                nav().navigateAction(R.id.action_mobile_to_web)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
            }
        }

        /**隐私政策点击事件*/
        val clickSpanPrivacy: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                nav().navigateAction(R.id.action_mobile_to_web)
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

    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnContinueLogin, mBind.btnResend, mBind.ivLoginBack) {
            when (it.id) {
                R.id.btn_continue_login -> {
                    if (isToLogin) {
                        loginTo()
                    } else {
                        getVerCode(1)
                    }
                }
                R.id.btn_resend -> {
                    getVerCode(2)
                }
                R.id.iv_login_back -> {
                    isToLogin = false
                    mBind.tvNumber.text = "Phone number"
                    mBind.tvEnterNumber.text = "Enter your phone\nnumber"
                    mBind.llOpt.visibility = View.GONE
                    mBind.llMobileNum.animation = AnimationUtils.makeInAnimation(appContext, true)
                    mBind.llMobileNum.visibility = View.VISIBLE
                    mBind.ivLoginBack.visibility = View.INVISIBLE
                    mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12_click)
                }
            }
        }
    }

    private fun loginTo() {
        val map = mapOf(
            EncryptUtil.encode("androidId") to androidId,
            EncryptUtil.encode("appInstanceId") to "",
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerId") to 0,
            EncryptUtil.encode("customerMobile") to mViewModel.mobileNum.get(),
            EncryptUtil.encode("deviceId") to androidId,
            EncryptUtil.encode("deviceType") to DeviceUtil.model,
            EncryptUtil.encode("fcmToken") to "",
            EncryptUtil.encode("ip") to "",
            EncryptUtil.encode("latitude") to 0,
            EncryptUtil.encode("longitude") to 0,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID,
            EncryptUtil.encode("noteToken") to noteToken,
            EncryptUtil.encode("otpCode") to mViewModel.deal.get(),
            EncryptUtil.encode("regGaid") to "",
        )
        val params = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody =
            RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                JSONObject(EncryptUtil.encryptBody(params)).toString()
            )
        when {
            mViewModel.deal.get().length != 6 -> showDialogMessage("Enter your phone OTP")
            !mBind.checkboxDeal.isChecked -> showDialogMessage("Please selected...")
            else -> {
                mViewModel.loginCallBack(paramsBody)?.observe(this) {
                    val mResponse = parseData(it)
                    if (mResponse.isNotEmpty()) {
                        LiveDataEvent.loginEvent.value = true //通知登录成功
                        CacheUtil.setIsLogin(true)
                        KvUtils.encode(Constant.ISLOGIN, true)
                        val loginBean = LoginInfoResponse()
                        val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                        if (data != null) {
                            loginBean.g = data.getBoolean("11")
                            loginBean.id = data.getInt("1F12")
                            loginBean.blackCustomer = data.getBoolean("141A17151D35030502191B1304")
                            loginBean.customerUid = data.getString("15030502191B1304231F12")
                            loginBean.token = data.getString("02191D1318")
                            loginBean.googleTester = data.getBoolean("111919111A13221305021304")
                            loginBean.customerMobile = data.getString("15030502191B13043B19141F1A13")
                        }
                        KvUtils.encode(Constant.TOKEN, loginBean.token)
                        KvUtils.encode(Constant.CUSTOMER_ID, loginBean.id)
                        startActivity<MainActivity>()
                        activity?.finish()
                    }
                }
            }
        }
    }

    private fun getVerCode(type: Int) {//1:发送验证码;2:重新发送验证码
        val map = mapOf(
            EncryptUtil.encode("androidId") to androidId,
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerAge") to "",
            EncryptUtil.encode("customerId") to 0,
            EncryptUtil.encode("customerMobile") to mViewModel.mobileNum.get(),
            EncryptUtil.encode("customerName") to "",
            EncryptUtil.encode("customerVa1") to "",
            EncryptUtil.encode("gaid") to "",
            EncryptUtil.encode("isVoice") to true,
            EncryptUtil.encode("marketId") to 10033
        )

        var parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody =
            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(EncryptUtil.encryptBody(parmas)).toString())
        when {
            mViewModel.mobileNum.get()
                .startsWith("0") && mViewModel.mobileNum.get().length != 11 -> showDialogMessage(
                "Enter your phone number"
            )
            !mViewModel.mobileNum.get()
                .startsWith("0") && mViewModel.mobileNum.get().length != 10 -> showDialogMessage(
                "Enter your phone number"
            )
            !mViewModel.isChecked.get() -> showDialogMessage("Please selected...")
            else -> {
                mViewModel.customerOtpCallBack(paramsBody)?.observe(this) {
                    val mResponse = parseData(it)
                    if (mResponse.isNotEmpty()) {
                        when (type) {
                            1 -> {
                                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                                noteToken = JSONObject(data.toString()).getString("1819021322191D1318")
//                              nav().navigateAction(R.id.action_mobile_to_code, Bundle().apply {
//                              putString(Constant.MOBILE_NUMBER, mViewModel.mobileNum.get())
//                              putString(Constant.NOTETOKEN, token)
//                              })
                                mBind.llMobileNum.visibility = View.GONE
                                mBind.llOpt.animation = AnimationUtils.makeInAnimation(appContext, false)
                                mBind.llOpt.visibility = View.VISIBLE
                                mBind.ivLoginBack.visibility = View.VISIBLE
                                mBind.tvNumber.text = "Confirmation"
                                mBind.tvEnterNumber.text = "Enter the code"
                                mBind.btnContinueLogin.setBackgroundResource(R.drawable.round_btn_12)
                                isToLogin = true
                            }
                            2 -> {

                            }
                        }

                    }
                }
            }
        }
    }

    override fun onRequestError(loadStatus: LoadStatusEntity) {
        when (loadStatus.requestCode) {
            NetUrl.CUSTOMER_OTP -> {
                showDialogMessage(loadStatus.errorMessage)
            }
        }
    }

}

