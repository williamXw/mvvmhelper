package com.gexiaobao.hdw.bw.ui.fragment.loan

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.dialog.RxLoanDialogSure
import com.gexiaobao.hdw.bw.app.util.EncryptUtil
import com.gexiaobao.hdw.bw.app.util.KvUtils
import com.gexiaobao.hdw.bw.app.util.RxToast
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentKycLoanBinding
import com.gexiaobao.hdw.bw.ui.view.PeterTimeCountRefresh
import com.gexiaobao.hdw.bw.ui.viewmodel.LoanFragmentViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject
import java.text.DecimalFormat

/**
 *  author : huxiaowei
 *  date : 2022/9/29 20:58
 *  description : 认证之后的首页
 */
class LoanKYCFragment : BaseFragment<LoanFragmentViewModel, FragmentKycLoanBinding>() {

    private lateinit var timeCount: PeterTimeCountRefresh

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        fetchHomeInfo()
    }

    private fun fetchHomeInfo() {
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody =
            RequestBody.create("application/json; charset=utf-8".toMediaTypeOrNull(), JSONObject(EncryptUtil.encryptBody(parmas)).toString())
        mViewModel.fetchHomeInfo(paramsBody)?.observe(this) {
            parseDataNoResult(it)
        }
    }

    override fun onResume() {
        super.onResume()
        timeCount = PeterTimeCountRefresh(mBind.tvLoanKycTimeCount, 180000, 1000)
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnLoanNow) {
            when (it) {
                mBind.btnLoanNow -> {
                    showLoanSureDialog()
//                    timeCountThree()
                }
            }
        }
    }

    private fun showLoanSureDialog() {
        val rxDialog = RxLoanDialogSure(context)
        rxDialog.setFullScreenWidth()
        rxDialog.btnConfirm.setOnClickListener {
            RxToast.showToast("点击了")
        }
        rxDialog.show()
    }

    private fun timeCountThree() {
        timeCount.setOnTimerFinishListener(object : PeterTimeCountRefresh.OnTimerFinishListener {
            override fun onTimerFinish() {
                RxToast.info("-------------->>>>>>>>><<<<<<<<")
            }

        })
        timeCount.setOnTimerProgressListener(object : PeterTimeCountRefresh.OnTimerProgressListener {
            override fun onTimerProgress(millisUntilFinished: Long) {
                val dec = DecimalFormat("##.##")
                var sec = ""
                sec = if (dec.format((millisUntilFinished % 60000) / 1000).equals(0) || dec.format((millisUntilFinished % 60000) / 1000)
                        .toInt() < 10
                ) {
                    "0" + dec.format((millisUntilFinished % 60000) / 1000)
                } else {
                    dec.format((millisUntilFinished % 60000) / 1000)
                }
                mBind.tvLoanKycTimeCount.text = "0" + (millisUntilFinished / 60000).toInt() + ":" + sec
            }

        })
        timeCount.start()
    }

    override fun onPause() {
        super.onPause()
        if (timeCount != null) {
            timeCount.cancel()
        }
    }

}