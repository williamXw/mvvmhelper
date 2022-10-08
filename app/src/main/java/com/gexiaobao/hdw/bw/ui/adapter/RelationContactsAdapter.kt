package com.gexiaobao.hdw.bw.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.gexiaobao.hdw.bw.R

/**
 * created by : huxiaowei
 * @date : 20220929
 * Describe :
 */
class RelationContactsAdapter(var mDataList: MutableList<String>) : RecyclerView.Adapter<RelationContactsAdapter.ViewHolder>() {

    private var mPosition = -1

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvItemName: AppCompatTextView = view.findViewById(R.id.tv_item_relation_name)
        var ivItemIcon: AppCompatImageView = view.findViewById(R.id.iv_item_relation_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_relation_contact, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            mPosition = holder.adapterPosition
            holder.ivItemIcon.visibility = View.VISIBLE
            notifyDataSetChanged()
            onItemClick?.setOnItemClick(mPosition)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvItemName.text = mDataList[position]
        if (position == mPosition) {
            holder.ivItemIcon.visibility = View.VISIBLE
        } else {
            holder.ivItemIcon.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mDataList.size
    }

    interface OnItemClick {
        fun setOnItemClick(position: Int)
    }

    private var onItemClick: OnItemClick? = null

    fun setOnItemClick(onItemClick: OnItemClick?) {
        this.onItemClick = onItemClick
    }

}