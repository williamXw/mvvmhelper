package com.gexiaobao.hdw.bw.ui.fragment.repayment

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentRepaymentDetailBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.RepaymentFragmentViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * created by : hxw
 * @date : 20221003
 * Describe : 获取还款详情信息
 */
class RepaymentDetailsFragment :
    BaseFragment<RepaymentFragmentViewModel, FragmentRepaymentDetailBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        getRepayRequest()
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnRepayment, mBind.btnDeferPayment) {
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
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {

            }
        }
    }
}