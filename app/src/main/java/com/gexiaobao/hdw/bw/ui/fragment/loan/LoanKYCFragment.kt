package com.gexiaobao.hdw.bw.ui.fragment.loan

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.dialog.RxDialogSure
import com.gexiaobao.hdw.bw.app.dialog.RxLoanDialogSure
import com.gexiaobao.hdw.bw.app.util.RxToast
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentKycLoanBinding
import com.gexiaobao.hdw.bw.ui.view.PeterTimeCountRefresh
import com.gexiaobao.hdw.bw.ui.viewmodel.LoanFragmentViewModel
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
//                    showLoanSureDialog()
                    timeCountThree()
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