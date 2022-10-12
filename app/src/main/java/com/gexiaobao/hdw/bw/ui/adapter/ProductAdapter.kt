package com.gexiaobao.hdw.bw.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.data.response.ProductsListResponse

/**
 *  author : huxiaowei
 *  date : 2022/10/12 21:49
 *  description :
 */
class ProductAdapter(data: ArrayList<ProductsListResponse>) :
    BaseQuickAdapter<ProductsListResponse, BaseViewHolder>(
        R.layout.item_product
    ) {

    override fun convert(holder: BaseViewHolder, item: ProductsListResponse) {
        holder.setText(R.id.tv_account_name, item.productName)
        holder.setText(R.id.tv_account_loan, item.loanAmount.toString())
    }

}