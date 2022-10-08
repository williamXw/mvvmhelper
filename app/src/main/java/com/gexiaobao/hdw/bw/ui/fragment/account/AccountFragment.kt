package com.gexiaobao.hdw.bw.ui.fragment.account

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentAccountBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.AccountFragmentViewModel

/**
 * created by : huxiaowei
 * @date : 20220920
 * Describe :
 */
class AccountFragment : BaseFragment<AccountFragmentViewModel, FragmentAccountBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.llLoanRecords, mBind.llMySetting, mBind.llAccountAboutUs, mBind.llDeleteIndividualData) {
            when (it) {
                mBind.llLoanRecords -> {
                }
                mBind.llMySetting -> {
                    nav().navigateAction(R.id.action_account_to_setting)
                }
                mBind.llAccountAboutUs -> {
                    nav().navigateAction(R.id.action_account_to_about)
                }
                mBind.llDeleteIndividualData -> {
                    nav().navigateAction(R.id.action_account_to_delete_data)
                }
            }
        }
    }
}