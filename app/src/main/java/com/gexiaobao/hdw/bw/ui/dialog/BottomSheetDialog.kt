package com.gexiaobao.hdw.bw.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.databinding.ActivityLoginBinding.inflate
import com.gexiaobao.hdw.bw.databinding.DialogMyBottomSheetBinding
import com.gexiaobao.hdw.bw.ui.adapter.RelationContactsAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import me.hgj.mvvmhelper.ext.inflateBinding

class BottomSheetDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogMyBottomSheetBinding
    private lateinit var adapter: RelationContactsAdapter
    private val mDataList = listOf<String>("Father/Mother", "Husband/Wife", "Son/Daughter", "Friend")

    /**
     * setStyle 圆角效果
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogMyBottomSheetBinding.inflate(inflater)
        initView()
        return binding.root
    }

    private fun initView() {
        adapter = RelationContactsAdapter(mDataList as MutableList<String>)
        binding.rvRelationContact.layoutManager = LinearLayoutManager(context)
        binding.rvRelationContact.adapter = adapter

        adapter.setOnItemClick(object : RelationContactsAdapter.OnItemClick {
            override fun setOnItemClick(position: Int) {
                onItemClick?.setOnItemClickListener(mDataList[position])
            }
        })
    }

    interface OnItemClickRe {
        fun setOnItemClickListener(content: String)
    }

    private var onItemClick: OnItemClickRe? = null

    fun setOnItemClickListener(onItemClickRe: OnItemClickRe) {
        this.onItemClick = onItemClickRe
    }

    /**
     * 设置固定高度
     */
    override fun onStart() {
        super.onStart()
//        //拿到系统的 bottom_sheet
//        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
//        //获取behavior
//        val behavior = BottomSheetBehavior.from(view)
//        //设置弹出高度
//        behavior.peekHeight = 950
    }
}