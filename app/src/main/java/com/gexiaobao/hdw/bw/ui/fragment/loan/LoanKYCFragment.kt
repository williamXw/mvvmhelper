package com.gexiaobao.hdw.bw.ui.fragment.loan

import android.os.Bundle
import android.util.Log
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.dialog.RxLoanDialogSure
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.FragmentKycLoanBinding
import com.gexiaobao.hdw.bw.ui.view.PeterTimeCountRefresh
import com.gexiaobao.hdw.bw.ui.viewmodel.LoanFragmentViewModel
import me.hgj.mvvmhelper.net.interception.logging.util.LogUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json.JSONObject
import java.text.DecimalFormat

/**
 *  author : huxiaowei
 *  date : 2022/9/29 20:58
 *  description : 认证之后的首页
 */
class LoanKYCFragment : BaseFragment<LoanFragmentViewModel, FragmentKycLoanBinding>() {

    private lateinit var timeCount: PeterTimeCountRefresh
    private var peroidUnit = 0//周期单位 10：天  20：月

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
            val mResponse = parseData2(it)
            if (mResponse.isNotEmpty()) {
                val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA)
                if (data != null) {
                    val bbbbb = EncryptUtil.decode("051304001F1513371B19031802")//利息
                    val bbbbbb = EncryptUtil.decode("1B1918021E0524170213")//月利率

                    peroidUnit = data.getInt("061304191F1223181F02")
                    mViewModel.amount.set(RxDataTool.getAmountValue(data.getDouble("1A191718371B19031802")))//借款金额
                    mViewModel.period.set(data.getString("061304191F12"))//借款周期
                    mViewModel.amountReceived.set("₹ " + data.getString("041315131F0013371B19031802"))//到账金额
                    mViewModel.youRepay.set("₹ " + data.getString("061304191F1223181F02"))
                    mViewModel.repaymentDate.set(getRepaymentDay(peroidUnit, data.getString("061304191F12").toInt()))
                    mViewModel.annualInterestRate.set(data.getString("0F13170424170213") + "%")//年利率
                }
            }
        }
    }

    private fun getRepaymentDay(unit: Int, num: Int): String {
        val year = DateTime().year
        val month = DateTime().monthOfYear
        val day = DateTime().dayOfMonth
        val dateTime = DateTime(year, month, day, 0, 0, 0, 0)
        when (unit) {
            10 -> {
                return dateTime.plusDays(num).toString("yyyy-MM-dd")
            }
            20 -> {
                return dateTime.plusMonths(num).toString("yyyy-MM-dd")
            }
        }
        return ""
    }

    override fun onResume() {
        super.onResume()
        timeCount = PeterTimeCountRefresh(mBind.tvLoanKycTimeCount, 180000, 1000)
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.btnLoanNow, mBind.tvLoanOrders) {
            when (it) {
                mBind.btnLoanNow -> {
                    showLoanSureDialog()
//                    timeCountThree()
                }
                mBind.tvLoanOrders -> {
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