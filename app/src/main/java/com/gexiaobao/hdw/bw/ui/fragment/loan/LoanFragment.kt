package com.gexiaobao.hdw.bw.ui.fragment.loan

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentLoanBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.LoanFragmentViewModel
import com.gyf.immersionbar.ImmersionBar
import me.hgj.mvvmhelper.net.LoadStatusEntity

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe : 我的
 */
class LoanFragment : BaseFragment<LoanFragmentViewModel, FragmentLoanBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        ImmersionBar.with(this).keyboardEnable(true).init()
        mBind.viewmodel = mViewModel
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.lanRequest) {
            when (it) {
                mBind.lanRequest -> {
                    nav().navigateAction(R.id.action_loan_to_indentification)
                }
            }
        }
    }

    override fun onRequestSuccess() {
        super.onRequestSuccess()
    }

    override fun onRequestError(loadStatus: LoadStatusEntity) {
        super.onRequestError(loadStatus)
    }

}