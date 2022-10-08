package com.gexiaobao.hdw.bw.ui.fragment.account

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentSettingMyBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.AccountFragmentViewModel

/**
 * created by : huxiaowei
 * @date : 20221002
 * Describe :
 */
class MySettingFragment : BaseFragment<AccountFragmentViewModel, FragmentSettingMyBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.llUserInfo) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.llUserInfo -> {
                    nav().navigateAction(R.id.action_my_setting_to_my_information)
                }
            }
        }
    }
}