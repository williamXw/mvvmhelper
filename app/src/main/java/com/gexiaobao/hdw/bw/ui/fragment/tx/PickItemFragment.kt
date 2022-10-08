package com.gexiaobao.hdw.bw.ui.fragment.tx

import android.os.Bundle
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.data.response.PickItemResponse
import com.gexiaobao.hdw.bw.databinding.FragmentPickItemBinding
import com.gexiaobao.hdw.bw.ui.adapter.PickItemAdapter
import com.gexiaobao.hdw.bw.ui.viewmodel.PickItemViewModel
import me.hgj.mvvmhelper.ext.divider
import me.hgj.mvvmhelper.ext.grid
import me.hgj.mvvmhelper.util.decoration.DividerOrientation
import kotlin.collections.listOf as listOf1

/**
 * created by : huxiaowei
 * @date : 20220921
 * Describe : 天祥-选择指定项目
 */
class PickItemFragment : BaseFragment<PickItemViewModel, FragmentPickItemBinding>() {

    private val pickItemAdapter: PickItemAdapter by lazy { PickItemAdapter() }
    private var data = ArrayList<PickItemResponse>()
    private val text = arrayOf("指定赛", "团体赛", "精英赛", "提交历史")
    private val image = arrayOf(R.mipmap.icon_logo, R.mipmap.icon_logo, R.mipmap.icon_logo, R.mipmap.icon_logo)

    override fun initView(savedInstanceState: Bundle?) {

        for (index in text.indices) {
            data.add(PickItemResponse(text[index], image[index]))
        }

        mBind.rvPickItem.grid(3).divider {
            orientation = DividerOrientation.GRID
            includeVisible = true
        }.adapter = pickItemAdapter

        pickItemAdapter.data = data
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.llBack) {
            when (it) {
                mBind.llBack -> {
                    nav().navigateUp()
                }
            }
        }
    }
}