package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentBankaccountDetailBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * created by : huxiaowei
 * @date : 20220929
 * Describe :
 */
class BankAccountDetailsFragment :
    BaseFragment<IdentificationViewModel, FragmentBankaccountDetailBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        mViewModel.title.set("Bank account details")
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnNext, mBind.etNameAdahaar) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.etNameAdahaar -> {
                    fetchBankList()
                }
            }
        }
    }

    private fun fetchBankList() {
        val map = mapOf<String, String>()
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody =
            RequestBody.create(
                "application/json; charset=utf-8".toMediaTypeOrNull(),
                JSONObject(EncryptUtil.encryptBody(parmas)).toString()
            )
        mViewModel.fetchBanks(paramsBody)?.observe(this) {
            val mResponse = parseData2(it)
            showBankListDialog()
            if (mResponse.isNotEmpty()) {

            }
        }
    }

    private fun showBankListDialog() {

    }
}