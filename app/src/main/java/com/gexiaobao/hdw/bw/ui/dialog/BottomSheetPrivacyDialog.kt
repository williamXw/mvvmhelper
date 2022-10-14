package com.gexiaobao.hdw.bw.ui.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.ext.RxWebViewTool
import com.gexiaobao.hdw.bw.app.util.CacheUtil
import com.gexiaobao.hdw.bw.app.util.DeviceUtil
import com.gexiaobao.hdw.bw.app.util.startActivity
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.DialogBottomSheetPrivacyBinding
import com.gexiaobao.hdw.bw.ui.activity.MainActivity
import com.gexiaobao.hdw.bw.ui.activity.PrivacyAgreementActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.permissionx.guolindev.PermissionX
import com.tamsiree.rxkit.interfaces.OnWebViewLoad
import com.tencent.bugly.crashreport.CrashReport
import me.hgj.mvvmhelper.base.appContext
import kotlin.system.exitProcess

class BottomSheetPrivacyDialog(private val privacyUrl: String, private val isInitFirst: Boolean) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogBottomSheetPrivacyBinding

    /**
     * setStyle 圆角效果
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = DialogBottomSheetPrivacyBinding.inflate(inflater)
        initView()
        return binding.root
    }

    private fun initView() {
        if (isInitFirst) {
            binding.ivClose.visibility = View.VISIBLE
        } else {
            binding.ivClose.visibility = View.GONE
        }

        RxWebViewTool.initWebView(context as Activity, binding.wvPrivacy, object : OnWebViewLoad {
            override fun onPageStarted() {

            }

            override fun onReceivedTitle(title: String) {}
            override fun onProgressChanged(newProgress: Int) {

            }

            override fun shouldOverrideUrlLoading() {}
            override fun onPageFinished() {

            }
        })
        binding.wvPrivacy.loadUrl(privacyUrl)

        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnDisagree.setOnClickListener {
            dismiss()
            activity?.finish()
        }

        binding.checkboxPrivacy.setOnCheckedChangeListener { _, b ->
            if (b) {
                CacheUtil.setIsAgreePrivacy(true)//表示已经点击同意了隐私政策
                dismiss()
            }
        }
    }

    private fun initBugly() {
        /**
         * 配置Bugly,根据官方提示，最好在获取权限之后初始化bugly，并且最好不要再异步线程
         * 参数3：建议在测试阶段建议设置成true，发布时设置为false
         */
        CrashReport.initCrashReport(
            appContext,
            Constant.APP_ID_BUGLY,
            true
        )
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
        setupRatio(requireContext(), dialog as BottomSheetDialog, 100)
        val view = view
        view?.post(Runnable {
            val behavior = dialog?.let { BottomSheetBehavior.from(it.findViewById(R.id.design_bottom_sheet)) };
            behavior?.isHideable = false//此处设置表示禁止BottomSheetBehavior的执行\
        })
    }

    private fun setupRatio(context: Context, bottomSheetDialog: BottomSheetDialog, percetage: Int) {
        val bottomSheet = bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
        val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = getBottomSheetDialogDefaultHeight(context, percetage)
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun getBottomSheetDialogDefaultHeight(context: Context, percetage: Int): Int {
        return getWindowHeight(context) * percetage / 100
    }

    private fun getWindowHeight(context: Context): Int {
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)
        dialog.setCanceledOnTouchOutside(false)
    }
}