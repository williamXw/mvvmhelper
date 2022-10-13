package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.data.response.CustomerCardInfo
import com.gexiaobao.hdw.bw.databinding.FragmentIndentificationNoBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 *  author : huxiaowei
 *  date : 2022/9/25 13:39
 *  description :
 */
class IdentificationNoFragment : BaseFragment<IdentificationViewModel, FragmentIndentificationNoBinding>() {

    private var idCardNumber = ""
    private var idCardName = ""

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
    }

    override fun initData() {
        super.initData()
        mViewModel.title.set("Identification")
        arguments?.run {
            getParcelable<CustomerCardInfo>("cardInfo").let {
                if (it != null) {
                    mViewModel.panNo.set(it.panNo)
                    mViewModel.aadhaarNo.set(it.aadHaarNo)
                    mViewModel.dateOfBirth.set(it.dateOfBirth)
                    mViewModel.gender.set(it.gender)
                    mViewModel.pincode.set(it.pinCode)
                    mViewModel.address.set(it.address)
                    idCardNumber = it.idCardNumber
                    idCardName = it.idCardName
                }
            }
        }
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnContinue) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.btnContinue -> {
                    submitAdJustInfo()
                }
            }
        }
    }

    private fun submitAdJustInfo() {
        val map = mapOf(
            EncryptUtil.encode("address") to mViewModel.address.get(),
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("birthday") to mViewModel.dateOfBirth.get(),
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("email") to mViewModel.email.get(),
            EncryptUtil.encode("idCardNumber") to idCardNumber,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID,
            EncryptUtil.encode("modifyIdCardName") to idCardName,
            EncryptUtil.encode("modifyPanName") to mViewModel.namePan.get(),
            EncryptUtil.encode("panNumber") to mViewModel.panNo.get()
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.submitAdJustInfo(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
//            nav().navigateAction(R.id.action_inden_no_to_inden_face)
            nav().navigateAction(R.id.action_inden_no_to_inden_contacts)
        }
    }

}