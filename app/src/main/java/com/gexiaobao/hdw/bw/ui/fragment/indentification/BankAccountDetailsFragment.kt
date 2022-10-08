package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentBankaccountDetailBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel

/**
 * created by : huxiaowei
 * @date : 20220929
 * Describe :
 */
class BankAccountDetailsFragment : BaseFragment<IdentificationViewModel, FragmentBankaccountDetailBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        mViewModel.title.set("Bank account details")
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnNext) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
            }
        }
    }
}