package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import android.util.Log
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.api.NetUrl
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.response.LoginInfoResponse
import com.gexiaobao.hdw.bw.databinding.FragmentEmergencyContactsBinding
import com.gexiaobao.hdw.bw.ui.activity.MainActivity
import com.gexiaobao.hdw.bw.ui.adapter.RelationContactsAdapter
import com.gexiaobao.hdw.bw.ui.dialog.BottomSheetDialog
import com.gexiaobao.hdw.bw.ui.viewmodel.EmergencyContactsFragmentVM
import me.hgj.mvvmhelper.ext.showDialogMessage
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import rxhttp.wrapper.exception.ParseException

/**
 *  author : huxiaowei
 *  date : 2022/9/27 22:55
 *  description :紧急联系人
 */
class EmergencyContactsFragment : BaseFragment<EmergencyContactsFragmentVM, FragmentEmergencyContactsBinding>() {

    private lateinit var loginBean: LoginInfoResponse

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        mViewModel.title.set("Emergency Contacts")
        loginBean = LiveDataEvent.loginResult.value as LoginInfoResponse
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnNext, mBind.etRelationOne, mBind.etRelationTwo) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.etRelationOne -> {
                    showDialog(1)
                }
                mBind.etRelationTwo -> {
                    showDialog(2)
                }
                mBind.btnNext -> {
                    submit()
                }
            }
        }
    }

    private fun submit() {
        when {
            mViewModel.relation.get().isEmpty() -> showDialogMessage("please....")
            mViewModel.mobile.get().isEmpty() -> showDialogMessage("please....")
            mViewModel.relation2.get().isEmpty() -> showDialogMessage("please....")
            mViewModel.mobile2.get().isEmpty() -> showDialogMessage("please....")
            else -> {
                val map = mapOf(
                    EncryptUtil.encode("contact1Mobile") to mViewModel.mobile.get(),
                    EncryptUtil.encode("contact1Name") to "ss",
                    EncryptUtil.encode("contact1Relation") to mViewModel.relation.get(),
                    EncryptUtil.encode("contact2Mobile") to mViewModel.mobile2.get(),
                    EncryptUtil.encode("contact2Name") to "dd",
                    EncryptUtil.encode("contact2Relation") to mViewModel.relation2.get(),
                    EncryptUtil.encode("customerId") to "10033"
                )
                val parmas = EncryptUtil.encode(JSONObject(map).toString())
                val paramsBody =
                    RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(EncryptUtil.encryptBody(parmas)).toString())
                mViewModel.pushUrgencyContactCallBack(paramsBody)?.observe(this) {
                    parseData(it)
                }
            }
        }
    }

    private fun parseData(it: Response?) {
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
        if (code == NetUrl.SUCCESS_CODE) {
            val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA) as Boolean
            if (data) {
                nav().navigateAction(R.id.action_contacts_to_bank)
                RxToast.showToast("successful")
            }
        } else {
            showDialogMessage(msg)
            if (code != NetUrl.SUCCESS_CODE) {
                throw ParseException(code.toString(), msg, it)
            }
        }
    }

    private fun showDialog(type: Int) {
        val dialog = BottomSheetDialog()
        dialog.setOnItemClickListener(object : BottomSheetDialog.OnItemClickRe {
            override fun setOnItemClickListener(content: String) {
                when (type) {
                    1 -> {
                        mViewModel.relation.set(content)
                    }
                    2 -> {
                        mViewModel.relation2.set(content)
                    }
                }
                dialog.dismiss()
            }
        })
        activity?.let { it1 -> dialog.show(it1.supportFragmentManager, "contact") }
    }

}