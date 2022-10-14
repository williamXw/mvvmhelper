package com.gexiaobao.hdw.bw.app.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.gexiaobao.hdw.bw.R

/**
 * @author tamsiree
 * @date 2016/7/19
 * 确认 弹出框
 */
class RxUpVersionDialog : RxDialog {

    lateinit var tvVersionName: AppCompatTextView
        private set
    lateinit var ivClose: AppCompatImageView
        private set
    lateinit var tvVersionContent: AppCompatTextView
        private set
    lateinit var btnUpdateNow: AppCompatButton
        private set
    lateinit var progressBar: ProgressBar
        private set
    lateinit var tvUpDateProgressNum: AppCompatTextView
        private set

    constructor(context: Context?, themeResId: Int) : super(context!!, themeResId) {
        initView()
    }

    constructor(
        context: Context?,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context!!, cancelable, cancelListener) {
        initView()
    }

    constructor(context: Context?) : super(context!!) {
        initView()
    }

    constructor(context: Context?, alpha: Float, gravity: Int) : super(context, alpha, gravity) {
        initView()
    }

    fun setSureListener(listener: View.OnClickListener?) {
//        sureView.setOnClickListener(listener)
//        btnSure.setOnClickListener(listener)
    }

    fun setSure(content: String?) {
//        sureView.text = content
//        btnSure.text = content
    }

    fun setProgressBar(progress: Int?) {
        progress?.let { progressBar.progress = it }
        tvUpDateProgressNum.text = progress.toString() + "%"
    }

    @SuppressLint("MissingInflatedId")
    private fun initView() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_up_version, null)
        tvVersionName = dialogView.findViewById(R.id.tv_version_name)
        tvVersionContent = dialogView.findViewById(R.id.tv_version_content)
        btnUpdateNow = dialogView.findViewById(R.id.btn_update_now)
        ivClose = dialogView.findViewById(R.id.iv_up_date_close)
        progressBar = dialogView.findViewById(R.id.progress_bar_up_date_version)
        tvUpDateProgressNum = dialogView.findViewById(R.id.tv_up_date_num)

        setContentView(dialogView)
    }
}