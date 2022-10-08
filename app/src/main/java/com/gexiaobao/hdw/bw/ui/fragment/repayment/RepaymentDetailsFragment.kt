package com.gexiaobao.hdw.bw.ui.fragment.repayment

import android.os.Bundle
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentRepaymentDetailBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.RepaymentFragmentViewModel

/**
 * created by : hxw
 * @date : 20221003
 * Describe :
 */
class RepaymentDetailsFragment : BaseFragment<RepaymentFragmentViewModel, FragmentRepaymentDetailBinding>() {

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
            }
        }
    }
}