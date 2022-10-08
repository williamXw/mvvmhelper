package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentFacialVertficationBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.FacialVerificationFragmentVM

/**
 *  author : huxiaowei
 *  date : 2022/9/27 21:31
 *  description : 人脸识别
 */
class FacialVerificationFagment :
    BaseFragment<FacialVerificationFragmentVM, FragmentFacialVertficationBinding>() {

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