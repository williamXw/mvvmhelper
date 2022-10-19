package com.gexiaobao.hdw.bw.ui.fragment.repayment

import android.os.Bundle
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentExtendBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.RepaymentFragmentViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 *  author : huxiaowei
 *  date : 2022/10/19 22:13
 *  description :
 */
class ExtendFragment : BaseFragment<RepaymentFragmentViewModel, FragmentExtendBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        fetchRollRepayInfo()
    }

    private fun fetchRollRepayInfo() {
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
        mViewModel.fetchRollRepayInfo(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {

            }
        }
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack)
        {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
            }
        }
    }
}