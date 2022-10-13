package com.gexiaobao.hdw.bw.ui.fragment.indentification

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.base.BaseFragment
import com.gexiaobao.hdw.bw.app.util.*
import com.gexiaobao.hdw.bw.comm.RxConstants
import com.gexiaobao.hdw.bw.databinding.FragmentEmergencyContactsBinding
import com.gexiaobao.hdw.bw.ui.dialog.BottomSheetListDialog
import com.gexiaobao.hdw.bw.ui.viewmodel.EmergencyContactsFragmentVM
import me.hgj.mvvmhelper.ext.showDialogMessage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.json.JSONObject

/**
 *  author : huxiaowei
 *  date : 2022/9/27 22:55
 *  description :紧急联系人
 */
class EmergencyContactsFragment :
    BaseFragment<EmergencyContactsFragmentVM, FragmentEmergencyContactsBinding>() {

    private var contactName1: String = ""
    private var contactMobile1: String = ""
    private var contactName2: String = ""
    private var contactMobile2: String = ""

    override fun initView(savedInstanceState: Bundle?) {
        mBind.viewmodel = mViewModel
        mViewModel.title.set("Emergency Contacts")
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        setOnclickNoRepeat(
            mBind.ivBack,
            mBind.btnNext,
            mBind.etRelationOne,
            mBind.etRelationTwo,
            mBind.etContactOne,
            mBind.etContactTwo
        ) {
            when (it) {
                mBind.ivBack -> {
                    nav().navigateUp()
                }
                mBind.etRelationOne -> {
                    showDialog(1)
                }
                mBind.etRelationTwo -> {
                    showDialog(2)
                }
                mBind.btnNext -> {
                    submit()
                }
                mBind.etContactOne -> {
                    getContactInfo(1)
                }
                mBind.etContactTwo -> {
                    getContactInfo(2)
                }
            }
        }
    }

    private fun getContactInfo(type: Int) {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent) { it ->
            if (it != null) {
                val contactData: Uri? = it.data
                val cursor: Cursor? = contactData?.let { it1 ->
                    activity?.contentResolver?.query(
                        it1, null, null, null, null
                    )
                }
                val phoneMap: Map<String, String> = getContactPhone(cursor)
                if (!cursor?.isClosed!!) {
                    cursor?.close()
                }
                if (null != phoneMap && phoneMap.isNotEmpty()) {
                    val keySet = phoneMap.keys
                    val keyNo = phoneMap.values
                    if (null != keySet && keySet.isNotEmpty()) {
                        val keys: Array<Any> = keySet.toTypedArray()
                        when (type) {
                            1 -> {
                                contactName1 = keys[0] as String
                            }
                            2 -> {
                                contactName2 = keys[0] as String
                            }
                        }
                    }
                    if (null != keyNo && keyNo.isNotEmpty()) {
                        val keys: Array<Any> = keyNo.toTypedArray()
                        when (type) {
                            1 -> {
                                contactMobile1 = keys[0] as String
                            }
                            2 -> {
                                contactMobile2 = keys[0] as String
                            }
                        }
                    }
                    when (type) {
                        1 -> {
                            mViewModel.mobile.set("$contactName1: $contactMobile1")
                        }
                        2 -> {
                            mViewModel.mobile2.set("$contactName2: $contactMobile2")
                        }
                    }
                }
            }
        }
    }

    private fun getContactPhone(cursor: Cursor?): Map<String, String> {
        val resultMap = HashMap<String, String>()
        var phoneName: String? = "" // 姓名
        var mobilePhoneNo: String? = "" // 手机号
        if (null != cursor) {
            cursor.moveToFirst()

            // 获得联系人的ID号
            val idFieldIndex = cursor
                .getColumnIndex(ContactsContract.Contacts._ID)
            val contactId = cursor.getString(idFieldIndex)
            // 联系人姓名
            val idphoneNameIndex = cursor
                .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
            phoneName = cursor.getString(idphoneNameIndex)

            // 获得联系人的电话号码的cursor
            val allPhones: Cursor = activity?.contentResolver?.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(contactId), null
            )!!

            // 所以联系电话（包话电话和手机号）
            val allPhoneNumList: MutableList<String> = ArrayList()
            if (allPhones.moveToFirst()) {

                // 遍历所有的电话号码
                while (!allPhones.isAfterLast) {
                    val telNoTypeIndex = allPhones
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE)
                    val telNoType = allPhones.getInt(telNoTypeIndex)
                    val telNoIndex = allPhones
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val telNo = allPhones.getString(telNoIndex)
                    allPhoneNumList.add(telNo)
                    if (2 == telNoType) { // 手机号（原生态的SDK定义：mobile是2，home是1，work是3，other是7）
                        mobilePhoneNo = telNo
                        break
                    }
                    allPhones.moveToNext()
                }
                if (!allPhones.isClosed) {
                    allPhones.close()
                }
            }
            if (!cursor.isClosed) {
                cursor.close()
            }
            resultMap[phoneName] = mobilePhoneNo.toString()
        }
        return resultMap
    }

    private fun submit() {
        nav().navigateAction(R.id.action_contacts_to_bank)
        when {
            mViewModel.relation.get().isEmpty() -> showDialogMessage("please....")
            mViewModel.mobile.get().isEmpty() -> showDialogMessage("please....")
            mViewModel.relation2.get().isEmpty() -> showDialogMessage("please....")
            mViewModel.mobile2.get().isEmpty() -> showDialogMessage("please....")
            else -> {
                val map = mapOf(
                    EncryptUtil.encode("contact1Mobile") to "1234567890",
                    EncryptUtil.encode("contact1Name") to contactName1,
                    EncryptUtil.encode("contact1Relation") to mViewModel.relation.get(),
                    EncryptUtil.encode("contact2Mobile") to "1234567890",
                    EncryptUtil.encode("contact2Name") to contactName2,
                    EncryptUtil.encode("contact2Relation") to mViewModel.relation2.get(),
                    EncryptUtil.encode("customerId") to customerID
                )
                val parmas = EncryptUtil.encode(JSONObject(map).toString())
                val paramsBody =
                    RequestBody.create(
                        "application/json; charset=utf-8".toMediaTypeOrNull(),
                        JSONObject(EncryptUtil.encryptBody(parmas)).toString()
                    )
                mViewModel.pushUrgencyContactCallBack(paramsBody)?.observe(this) {
                    val mResponse = parseData(it)
                    if (mResponse.isNotEmpty()) {
                        val data = JSONObject(mResponse).getJSONObject(RxConstants.DATA) as Boolean
                        if (data) {
                            nav().navigateAction(R.id.action_contacts_to_bank)
                            RxToast.showToast("successful")
                        }
                    }
                }
            }
        }
    }

    private fun showDialog(type: Int) {
        val mDataList = listOf("Father/Mother", "Husband/Wife", "Son/Daughter", "Friend")
        val dialog = BottomSheetListDialog(mDataList, "Relationship")
        dialog.setOnItemClickListener(object : BottomSheetListDialog.OnItemClickRe {
            override fun setOnItemClickListener(content: String) {
                when (type) {
                    1 -> {
                        mViewModel.relation.set(content)
                    }
                    2 -> {
                        mViewModel.relation2.set(content)
                    }
                }
                dialog.dismiss()
            }
        })
        activity?.let { it1 -> dialog.show(it1.supportFragmentManager, "contact") }
    }

}