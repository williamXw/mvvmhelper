package com.gexiaobao.hdw.bw.ui.fragment.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.addTextChangedListener
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.ext.hideSoftKeyboard
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.app.util.RxTextTool.getBuilder
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.data.response.ApiResponse
import com.gexiaobao.hdw.bw.databinding.FragmentLoginBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.LoginViewModel
import com.gyf.immersionbar.ImmersionBar
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.ext.toJsonStr
import me.hgj.mvvmhelper.net.LoadStatusEntity
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.wait
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.utils.convert

/**
 * created by : huxiaowei
 * @date : 20220913
 * Describe :
 */
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {

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
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnContinueLogin) {
            when (it.id) {
                R.id.btn_continue_login -> {
                    getVerCode()
                }
            }
        }
    }

    private fun getVerCode() {
        val androidId = context?.let { it1 -> DeviceUtil.getAndroidId(it1) }
        val appVersion = activity?.let { it1 -> DeviceUtil.getVersionCode(it1) }

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
                    parseData(it)
                }
            }
        }
    }

    private fun parseData(it: Response) {
        var code = -1
        var msg = ""
        var token = ""
        var mResponse = ""
        if (it.code == 200) {
            val dataBody = JSONObject(it.body!!.string())
            val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
            val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
            val thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
            mResponse = EncryptUtil.decode(thirdKey)
            msg = JSONObject(mResponse).getString(RxConstants.MSG)
            code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
        } else {
            LogUtils.debugInfo(it.code.toString() + it.message)
        }
        hideSoftKeyboard(activity)
        if (code == NetUrl.SUCCESS_CODE) {
            val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
            token = JSONObject(data.toString()).getString("1819021322191D1318")
            nav().navigateAction(R.id.action_mobile_to_code, Bundle().apply {
                putString(Constant.MOBILE_NUMBER, mViewModel.mobileNum.get())
                putString(Constant.NOTETOKEN, token)
            })
        } else {
            if (code != NetUrl.SUCCESS_CODE) {
                throw ParseException(code.toString(), msg, it)
            }
            showDialogMessage(msg)
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

