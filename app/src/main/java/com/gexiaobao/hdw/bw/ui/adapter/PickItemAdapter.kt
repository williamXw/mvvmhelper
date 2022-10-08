package com.gexiaobao.hdw.bw.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.data.response.PickItemResponse

/**
 * created by : huxiaowei
 * @date : 20220921
 * Describe :
 */
class PickItemAdapter : BaseQuickAdapter<PickItemResponse, BaseViewHolder>(R.layout.item_pick_item), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: PickItemResponse) {
        holder.setText(R.id.tv_item_pick_text, item.text)

    }
}