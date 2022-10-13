package com.gexiaobao.hdw.bw.ui.dialog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.ext.LiveDataEvent
import com.gexiaobao.hdw.bw.app.util.CacheUtil
import com.gexiaobao.hdw.bw.app.util.DeviceUtil
import com.gexiaobao.hdw.bw.app.util.RxToast
import com.gexiaobao.hdw.bw.app.util.startActivity
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.databinding.DialogBottomSheetPrivacyBinding
import com.gexiaobao.hdw.bw.ui.activity.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.permissionx.guolindev.PermissionX
import com.tencent.bugly.crashreport.CrashReport
import me.hgj.mvvmhelper.base.appContext
import kotlin.system.exitProcess

class BottomSheetPrivacyDialog(val privacyUrl: String) : BottomSheetDialogFragment() {

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
        binding.ivClose.setOnClickListener {
            dismiss()
        }

        binding.btnDisagree.setOnClickListener {
            dismiss()
            activity?.finish()
        }

        binding.checkboxPrivacy.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                dismiss()
                permissionRequest()
            }
        }
    }

    private fun permissionRequest() {
        PermissionX.init(this)
            .permissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_CONTACTS
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            //解释申请权限的用途，不需要则不用写
//            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList ->
                //如果权限被拒绝多个  只有相机是必须开启才能进入程序  则需要下面的代码过滤申请
//                val filteredList = deniedList.filter {
//                    it == Manifest.permission.CAMERA
//                }
                scope.showRequestReasonDialog(
                    deniedList,
                    DeviceUtil.getAppName(appContext) + "需要以下权限才能继续",
                    "同意",
                    "拒绝"
                )
            }.onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "您需要去应用程序设置当中手动开启权限", "好的")
            }
            .request { allGranted, _, deniedList ->
                if (allGranted) {
                    CacheUtil.setPermission(true)//同意了权限 记录
                    CacheUtil.setInitFirst(false)//同意权限之后 设置状态
                    startActivity<MainActivity>()
                    activity?.finish()
                    //初始化Bugly
                    initBugly()
                } else {
                    CacheUtil.setPermission(false)//拒绝了某个权限 记录
                    exitProcess(0)
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
//        //拿到系统的 bottom_sheet
//        val view: FrameLayout = dialog?.findViewById(R.id.design_bottom_sheet)!!
//        //获取behavior
//        val behavior = BottomSheetBehavior.from(view)
//        //设置弹出高度
//        behavior.peekHeight = 1200
    }

    fun setupRatio(context: Context, bottomSheetDialog: BottomSheetDialog, percetage: Int) {
        //id = com.google.android.material.R.id.design_bottom_sheet for Material Components
        //id = android.support.design.R.id.design_bottom_sheet for support librares
        val bottomSheet =
            bottomSheetDialog.findViewById<View>(R.id.design_bottom_sheet) as FrameLayout
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
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}