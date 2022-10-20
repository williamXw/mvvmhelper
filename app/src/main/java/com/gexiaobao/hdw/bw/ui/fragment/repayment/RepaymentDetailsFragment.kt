package com.gexiaobao.hdw.bw.ui.fragment.repayment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.ext.countDownCoroutines
import com.gexiaobao.hdw.bw.app.ext.jumpByLogin
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentRepaymentDetailBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.RepaymentFragmentViewModel
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException
import java.util.Timer
import kotlin.concurrent.schedule

/**
 * created by : hxw
 * @date : 20221003
 * Describe : 获取还款详情信息
 */
class RepaymentDetailsFragment :
    BaseFragment<RepaymentFragmentViewModel, FragmentRepaymentDetailBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {
        super.initData()
        job = countDownCoroutines(1, {
        }, {
            getRepayRequest()
        }, lifecycleScope)
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(
            mBind.ivBack,
            mBind.btnRepayment,
            mBind.btnDeferPayment,
            mBind.llInputUtr
        ) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.btnRepayment -> {
                    confirmedPayment()
                }
                mBind.btnDeferPayment -> {//延期还款
                    nav().navigateAction(R.id.action_account_to_Extend)
                }
                mBind.llInputUtr -> {
                    nav().navigateAction(R.id.action_account_to_utr)
                }
            }
        }
    }

    /**
     * 发起还款
     */
    private fun confirmedPayment() {
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("borrowId") to 0,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("delayTime") to 0,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID,
            EncryptUtil.encode("payAmount") to 0,
            EncryptUtil.encode("payChannelChooseIndex") to 0,
            EncryptUtil.encode("payCompanyType") to 0,
            EncryptUtil.encode("repayType") to 0
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )

        mViewModel.fetchRepayLink(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {

            }
        }
    }

    private fun getRepayRequest() {
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("borrowId") to 0,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.getRepayByBorrow(paramsBody)?.observe(this) {
            if (it!!.code == 200) {
                val dataBody = JSONObject(it.body!!.string())
                val firstKey = JSONObject(dataBody.toString()).get(RxConstants.KEY).toString()
                val secondKey = JSONObject(firstKey).get(RxConstants.KEY).toString()
                var thirdKey = JSONObject(secondKey).get(RxConstants.KEY).toString()
                val mResponse = EncryptUtil.decode(thirdKey)
                Log.i("-------------->>>", mResponse)
                val msg = JSONObject(mResponse).getString(RxConstants.MSG)
                val code = JSONObject(mResponse).getString(RxConstants.CODE).toInt()
                if (code == NetUrl.SUCCESS_CODE) {

                } else if (code == NetUrl.LOGIN_OUT_CODE) {
                    nav().jumpByLogin { }
                } else {
                    showDialogMessage(msg)
                    if (code != NetUrl.SUCCESS_CODE) {
                        throw ParseException(code.toString(), msg, it)
                    }
                }
            } else {
                if (it != null) {
                    LogUtils.debugInfo(it.code.toString() + it.message)
                    throw ParseException(it.code.toString(), it.message, it)
                }
            }
        }
    }
}