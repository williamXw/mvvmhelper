package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.data.commom.Constant
import com.gexiaobao.hdw.bw.data.response.CustomerCardInfo
import com.gexiaobao.hdw.bw.databinding.FragmentIndentificationCardBinding
import com.gexiaobao.hdw.bw.ui.viewmodel.IdentificationViewModel
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject


/**
 *  author : huxiaowei
 *  date : 2022/9/25 13:39
 *  description :
 */
@RequiresApi(Build.VERSION_CODES.O)
class IdentificationFragment :
    BaseFragment<IdentificationViewModel, FragmentIndentificationCardBinding>() {

    private lateinit var bitmap: Bitmap
    private var imageBase64 = ""

    private var pinNO = ""
    private var aadHaarNo = ""
    private var dateOfBirth = ""
    private var gender = ""
    private var pinCode = ""
    private var address = ""
    private var idCardNumber = ""
    private var idCardName = ""

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
                    nav().navigateAction(R.id.action_inden_card_to_inden_no, Bundle().apply {
                        putParcelable(
                            "cardInfo", CustomerCardInfo(
                                pinNO,
                                aadHaarNo,
                                dateOfBirth,
                                gender,
                                pinCode,
                                address,
                                idCardNumber,
                                idCardName,
                            )
                        )
                    })
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
                                kycIDCardBackOrcRequest()
                            }
                            2 -> {
                                kycIDCardFrontOrcRequest()
                            }
                            3 -> {
                                panOcrRequest()
                            }
                        }
                    }
                }

                override fun onCancel() {}
            })
    }

    private fun panOcrRequest() {
        val paramsBody = getBody()
        mViewModel.kycIDCardPanOrc(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
        }
        mBind.ivCardPanImage.setImageBitmap(bitmap)
    }

    private fun kycIDCardFrontOrcRequest() {
        val paramsBody = getBody()
        mViewModel.kycIDCardFrontOrc(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
        }
        mBind.ivCardBackImage.setImageBitmap(bitmap)
    }

    private fun kycIDCardBackOrcRequest() {
        val paramsBody = getBody()
        mViewModel.kycIDCardBackOrc(paramsBody)?.observe(this) {
            val mResponse = parseData(it)
        }
        mBind.ivCardFontImage.setImageBitmap(bitmap)
    }

    private fun getBody(): RequestBody {
        if (bitmap != null) {
//            imageBase64 = UriUtils.zipString(UriUtils.byte2Base64(UriUtils.bitmap2Byte(bitmap))).toString()
            imageBase64 = UriUtils.compress(UriUtils.byte2Base64(UriUtils.bitmap2Byte(bitmap))).toString()
            val unCompress = UriUtils.unCompress(imageBase64)
        }
        val map = mapOf(
            EncryptUtil.encode("appVersion") to appVersion,
            EncryptUtil.encode("compressed") to true,
            EncryptUtil.encode("customerId") to customerID,
            EncryptUtil.encode("imageBase64") to imageBase64,
            EncryptUtil.encode("marketId") to Constant.MARKET_ID,
        )
        val parmas = EncryptUtil.encode(JSONObject(map).toString())
        val paramsBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            JSONObject(EncryptUtil.encryptBody(parmas)).toString()
        )
        return paramsBody
    }
}