package com.gexiaobao.hdw.bw.ui.fragment.repayment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.GlideEngine
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentInputUtrBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.RepaymentFragmentViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener

/**
 *  author : huxiaowei
 *  date : 2022/10/20 21:35
 *  description :
 */
class InputUtrFragment : BaseFragment<RepaymentFragmentViewModel, FragmentInputUtrBinding>() {

    private var bitmap: Bitmap? = null

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(mBind.ivBack, mBind.btnSubmitUtr, mBind.ivAddPictureNormal) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.btnSubmitUtr -> {
                    submitData()
                }
                mBind.ivAddPictureNormal -> {
                    selectedPicture()
                }
            }
        }
    }

    private fun submitData() {
        if (mViewModel.utrNo.get().isEmpty()) {
            mBind.etUtrNo.background = resources.getDrawable(R.drawable.round_red_12)
            mBind.tvPleaseEnterUtr.visibility = View.VISIBLE
        } else {
            mBind.etUtrNo.background = resources.getDrawable(R.drawable.round_white_12)
            mBind.tvPleaseEnterUtr.visibility = View.GONE
        }
        if (bitmap == null) {
            mBind.ivAddPictureNormal.background = resources.getDrawable(R.drawable.round_red_12)
            mBind.tvUploadUtrScreenshot.visibility = View.VISIBLE
        } else {
            mBind.ivAddPictureNormal.background = resources.getDrawable(R.drawable.round_white_12)
            mBind.tvUploadUtrScreenshot.visibility = View.GONE
        }

    }

    private fun selectedPicture() {
        PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(1)// ???????????????????????? int
            .isCamera(true)// ???????????????????????? true or false
            .compress(true)// ???????????? true or false
            .minimumCompressSize(100)// ??????100kb??????????????????
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (result != null) {
                        mBind.ivAddPictureNormal.visibility = View.GONE
                        mBind.ivAddPicture.visibility = View.VISIBLE
                        bitmap = BitmapFactory.decodeFile(result[0].compressPath)
                        mBind.ivAddPicture.setImageBitmap(bitmap)
                        mBind.ivAddPicture.scaleType = ImageView.ScaleType.CENTER_CROP
                    }
                }

                override fun onCancel() {}
            })
    }

}