package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.GlideEngine
import com.gexiaobao.hdw.bw.app.util.nav
import com.gexiaobao.hdw.bw.app.util.navigateAction
import com.gexiaobao.hdw.bw.app.util.setOnclickNoRepeat
import com.gexiaobao.hdw.bw.databinding.FragmentIndentificationCardBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener


/**
 *  author : huxiaowei
 *  date : 2022/9/25 13:39
 *  description :
 */
class IdentificationFragment :
    BaseFragment<IdentificationViewModel, FragmentIndentificationCardBinding>() {

    private lateinit var bitmap: Bitmap

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
    }

    override fun initData() {
        super.initData()
        mViewModel.title.set("Identification")
        mBind.ivCardPanImage.scaleType = ImageView.ScaleType.CENTER_CROP
        mBind.ivCardFontImage.scaleType = ImageView.ScaleType.CENTER_CROP
        mBind.ivCardBackImage.scaleType = ImageView.ScaleType.CENTER_CROP
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(
            mBind.ivBack,
            mBind.ivCardFontImage,
            mBind.ivCardBackImage,
            mBind.ivCardPanImage,
            mBind.btnContinue
        ) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.btnContinue -> {
                    nav().navigateAction(R.id.action_inden_card_to_inden_no)
                }
                mBind.ivCardFontImage -> {
                    selectedPicture(1)
                }
                mBind.ivCardBackImage -> {
                    selectedPicture(2)
                }
                mBind.ivCardPanImage -> {
                    selectedPicture(3)
                }
            }
        }
    }

    private fun selectedPicture(type: Int) {
        PictureSelector.create(activity)
            .openGallery(PictureMimeType.ofImage())
            .loadImageEngine(GlideEngine.createGlideEngine())
            .maxSelectNum(1)// 最大图片选择数量 int
            .isCamera(true)// 是否显示拍照按钮 true or false
            .compress(true)// 是否压缩 true or false
            .minimumCompressSize(100)// 小于100kb的图片不压缩
            .forResult(object : OnResultCallbackListener<LocalMedia> {
                override fun onResult(result: MutableList<LocalMedia>?) {
                    if (result != null) {
                        bitmap = BitmapFactory.decodeFile(result[0].compressPath)
                        when (type) {
                            1 -> {
                                mBind.ivCardFontImage.setImageBitmap(bitmap)
                            }
                            2 -> {
                                mBind.ivCardBackImage.setImageBitmap(bitmap)
                            }
                            3 -> {
                                mBind.ivCardPanImage.setImageBitmap(bitmap)
                            }
                        }
                    }
                }

                override fun onCancel() {}
            })
    }

    override fun onRequestSuccess() {
        super.onRequestSuccess()
    }
}