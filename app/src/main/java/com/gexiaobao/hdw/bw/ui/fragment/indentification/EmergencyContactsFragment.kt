package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentEmergencyContactsBinding
import com.gexiaobao.hdw.bw.ui.adapter.RelationContactsAdapter
import com.gexiaobao.hdw.bw.ui.dialog.BottomSheetDialog
import com.gexiaobao.hdw.bw.ui.viewmodel.EmergencyContactsFragmentVM
import me.hgj.mvvmhelper.ext.showDialogMessage

/**
 *  author : huxiaowei
 *  date : 2022/9/27 22:55
 *  description :紧急联系人
 */
class EmergencyContactsFragment : BaseFragment<EmergencyContactsFragmentVM, FragmentEmergencyContactsBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        mViewModel.title.set("Emergency Contacts")
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
                nav().navigateAction(R.id.action_contacts_to_bank)
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