package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentBankaccountDetailBinding
import com.gexiaobao.hdw.bw.ui.activity.MainActivity
import com.gexiaobao.hdw.bw.ui.dialog.BottomSheetListDialog
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel
import me.hgj.mvvmhelper.ext.showDialogMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 * created by : huxiaowei
 * @date : 20220929
 * Describe : 银行卡认证
 */
class BankAccountDetailsFragment :
    BaseFragment<IdentificationViewModel, FragmentBankaccountDetailBinding>() {

    private var mList = ArrayList<String>()

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        mViewModel.title.set("Bank account details")
    }

    override fun initData() {
        super.initData()

        mBind.etAccountNo.addTextChangedListener {
            if (mViewModel.enterAcNo.get() != mViewModel.reEnterAcNo.get() && mViewModel.reEnterAcNo.get().isNotEmpty()) {
                mBind.etAccountNo.background = resources.getDrawable(R.drawable.round_red_12)
                mBind.etReenterAccountNo.background = resources.getDrawable(R.drawable.round_red_12)
            } else {
                mBind.etAccountNo.background = resources.getDrawable(R.drawable.round_white_12)
                mBind.etReenterAccountNo.background = resources.getDrawable(R.drawable.round_white_12)
            }
        }

        mBind.etReenterAccountNo.addTextChangedListener {
            mViewModel.reEnterAcNo.set(it.toString())
            if (mViewModel.enterAcNo.get() != mViewModel.reEnterAcNo.get()) {
                mBind.etAccountNo.background = resources.getDrawable(R.drawable.round_red_12)
                mBind.etReenterAccountNo.background = resources.getDrawable(R.drawable.round_red_12)
            } else {
                mBind.etAccountNo.background = resources.getDrawable(R.drawable.round_white_12)
                mBind.etReenterAccountNo.background = resources.getDrawable(R.drawable.round_white_12)
            }
        }
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
                mBind.btnNext -> {
                    nextData()
                }
            }
        }
    }

    private fun nextData() {
        when {
            mViewModel.bankName.get().isEmpty() -> showDialogMessage("please enter Bank Name")
            mViewModel.beneficiaryName.get()
                .isEmpty() -> showDialogMessage("please enter Beneficiary Name")
            mViewModel.enterIFSCCode.get().isEmpty() -> showDialogMessage("please enter IFSCCode")
            mViewModel.enterAcNo.get().isEmpty() -> showDialogMessage("please enter Account No")
            mViewModel.reEnterAcNo.get().isEmpty() -> showDialogMessage("please ReEnter Account No")
            mViewModel.enterAcNo.get() != mViewModel.reEnterAcNo.get() -> showDialogMessage("The Account No. does not match.")
            mViewModel.enterIFSCCode.get().length != 11 -> showDialogMessage("Invalid IFSC code")
            mViewModel.enterIFSCCode.get().substring(4, 5).toInt() != 0 -> showDialogMessage("Invalid IFSC code")
            mViewModel.enterAcNo.get().length < 9 -> showDialogMessage("Invalid Account No")
            else -> {
                val a = mViewModel.enterIFSCCode.get().substring(4, 5).toInt()
                val map = mapOf(
                    EncryptUtil.encode("appVersion") to appVersion,
                    EncryptUtil.encode("bankAccount") to mViewModel.reEnterAcNo.get(),
                    EncryptUtil.encode("bankName") to mViewModel.bankName.get(),
                    EncryptUtil.encode("bankUserName") to mViewModel.beneficiaryName.get(),
                    EncryptUtil.encode("customerId") to customerID,
                    EncryptUtil.encode("ifsc") to mViewModel.enterIFSCCode.get(),
                    EncryptUtil.encode("marketId") to Constant.MARKET_ID,
                    EncryptUtil.encode("validateType") to 0
                )
                val parmas = EncryptUtil.encode(JSONObject(map).toString())
                val paramsBody = RequestBody.create(
                    "application/json; charset=utf-8".toMediaTypeOrNull(),
                    JSONObject(EncryptUtil.encryptBody(parmas)).toString()
                )
                mViewModel.bindBankCard(paramsBody)?.observe(this) {
                    val mResponse = parseData(it)
                    if (!mResponse.isNullOrEmpty()) {
                        val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                        val result = data.getBoolean("041305031A02")
                        if (result) {
                            startActivity<MainActivity>()
                            CacheUtil.setAuthenticationSucceed(true) //保存认证成功
                        }
                    }
                }
            }
        }
    }

    private fun fetchBankList() {
        val map = mapOf<String, String>()
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        mViewModel.fetchBanks(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
            if (mResponse.isNotEmpty()) {
                mList.clear()
                val data = JSONObject(mResponse).getJSONArray(RxConstants.DATA)
                for (i in 0 until data.length()) {
                    val result = data[i].toString()
                    val value = JSONObject(result).getString("1417181D38171B13")
                    mList.add(value)
                }
                showBankListDialog()
            }
        }
    }

    private fun showBankListDialog() {
        val dialog = BottomSheetListDialog(mList, "Banks")
        dialog.setOnItemClickListener(object : BottomSheetListDialog.OnItemClickRe {
            override fun setOnItemClickListener(content: String) {
                mViewModel.bankName.set(content)
                dialog.dismiss()
            }
        })
        activity?.let { it1 -> dialog.show(it1.supportFragmentManager, "contact") }
    }
}