package com.gexiaobao.hdw.bw.app.ext

import android.annotation.SuppressLint
import android.os.Parcelable
import com.gexiaobao.hdw.bw.R
import com.gexiaobao.hdw.bw.app.ext.AppUpdate
import android.os.Parcel

/**
 * @author hule
 * @date 2019/7/10 15:48
 * description:app下载更新的实体类,通过服务器返回的更新实体中取出赋值
 */
class AppUpdate : Parcelable {
    /**
     * 新版本的下载地址
     */
    var newVersionUrl: String?
        private set

    /**
     * 新版本号
     */
    var newVersionCode: String?
        private set

    /**
     * 是否采取强制更新，默认为0，不采取强制更新，否则强制更新
     */
    var forceUpdate: Int
        private set

    /**
     * 新版本更新的内容
     */
    var updateInfo: String?
        private set

    /**
     * 新版本文件的大小,单位一般为M，需要自己换算，因为不知道保留的位数，根据自己需求吧
     */
    var fileSize: String?
        private set

    /**
     * 文件下的保存路径 以/开头 比如 /A/B
     */
    var savePath: String?
        private set

    /**
     * 1.apk文件的md5值，用于校验apk文件签名是否一致，防止下载被拦截，
     * 2.用于校验文件大小的完整性
     * 3.若无MD5值，那么安装时不去校验
     */
    var md5: String?
        private set

    /**
     * 浏览器的下载地址，如果下载失败，通过浏览器下载
     */
    var downBrowserUrl: String?
        private set
    //    /**
    //     * 更新提示框的title提示语
    //     */
    //    private int updateTitle;
    //    /**
    //     * 更新内容的提示语
    //     */
    //    private int updateContentTitle;
    //
    //    /**
    //     * 更新按钮的文字
    //     */
    //    private int updateText;
    //    /**
    //     * 取消更新按钮的文字
    //     */
    //    private int updateCancelText;
    //
    //    /**
    //     * 更新按钮的颜色
    //     */
    //    private int updateColor;
    //    /**
    //     * 取消更新按钮的颜色
    //     */
    //    private int updateCancelColor;
    //
    //    /**
    //     * 下载进度条的颜色，二级进度
    //     */
    //    private int updateProgressColor;
    //    public int getUpdateTitle() {
    //        return updateTitle;
    //    }
    //
    //    public int getUpdateContentTitle() {
    //        return updateContentTitle;
    //    }
    //
    //    public int getUpdateText() {
    //        return updateText;
    //    }
    //
    //    public int getUpdateCancelText() {
    //        return updateCancelText;
    //    }
    //
    //    public int getUpdateColor() {
    //        return updateColor;
    //    }
    //
    //    public int getUpdateCancelColor() {
    //        return updateCancelColor;
    //    }
    //
    //    public int getUpdateProgressColor() {
    //        return updateProgressColor;
    //    }
    /**
     * 风格：true代表默认静默下载模式，只弹出下载更新框,下载完毕自动安装， false 代表配合使用进度框与下载失败弹框
     */
    var isSlentMode: Boolean
        private set

    /**
     * 更新对话框的id
     */
    var updateResourceId: Int
        private set

    private constructor(builder: Builder) {
        newVersionUrl = builder.newVersionUrl
        newVersionCode = builder.newVersionCode
        forceUpdate = builder.forceUpdate
        updateInfo = builder.updateInfo
        fileSize = builder.fileSize
        savePath = builder.savePath
        md5 = builder.md5
        downBrowserUrl = builder.downBrowserUrl
        //        this.updateTitle = builder.updateTitle;
//        this.updateContentTitle = builder.updateContentTitle;
//        this.updateText = builder.updateText;
//        this.updateCancelText = builder.updateCancelText;
//        this.updateColor = builder.updateColor;
//        this.updateCancelColor = builder.updateCancelColor;
//        this.updateProgressColor = builder.updateProgressColor;
        isSlentMode = builder.isSilentMode
        updateResourceId = builder.updateResourceId
    }

    /**
     * 构造者模式，链式调用，构建和表示分离，可读性好
     */
    class Builder {
        var newVersionUrl: String? = null
        var newVersionCode: String? = null

        /**
         * 默认不采取强制更新
         */
        var forceUpdate = 0
        var updateInfo: String? = null
        var fileSize: String? = null

        /**
         * 默认的保存路径
         */
        var savePath = "/download/"
        var md5: String? = null

        /**
         * 默认的市场下载地址
         */
        var downBrowserUrl = ""
        /**
         * 更新提示框的title提示语
         */
        //        private int updateTitle = R.string.update_title;
        /**
         * 更新内容的提示语
         */
        //        private int updateContentTitle = R.string.update_content_lb;
        /**
         * 默认的更新文本
         */
        //        private int updateText = R.string.update_text;
        /**
         * 默认的取消更新文本
         */
        //        private int updateCancelText = R.string.update_later;
        /**
         * 默认的更新颜色
         */
        //        private int updateColor = R.color.color_blue;
        /**
         * 默认的取消更新文本颜色
         */
        //        private int updateCancelColor = R.color.color_blue;
        /**
         * 默认的更新进度条颜色
         */
        //        private int updateProgressColor = R.color.color_blue;
        /**
         * 风格：true代表默认静默下载模式，只弹出下载更新框,下载完毕自动安装， false 代表配合使用进度框与下载失败弹框
         */
        var isSilentMode = true

        /**
         * 更新对话框的id
         */
        var updateResourceId = R.layout.dialog_up_version
        fun newVersionUrl(newVersionUrl: String?): Builder {
            this.newVersionUrl = newVersionUrl
            return this
        }

        fun newVersionCode(newVersionCode: String?): Builder {
            this.newVersionCode = newVersionCode
            return this
        }

        fun forceUpdate(forceUpdate: Int): Builder {
            this.forceUpdate = forceUpdate
            return this
        }

        fun updateInfo(updateInfo: String?): Builder {
            this.updateInfo = updateInfo
            return this
        }

        fun fileSize(fileSize: String?): Builder {
            this.fileSize = fileSize
            return this
        }

        fun savePath(saveFilePath: String): Builder {
            savePath = saveFilePath
            return this
        }

        fun md5(md5: String?): Builder {
            this.md5 = md5
            return this
        }

        fun downBrowserUrl(downBrowserUrl: String): Builder {
            this.downBrowserUrl = downBrowserUrl
            return this
        }

        //        public Builder updateTitle(int updateTitle) {
        //            this.updateTitle = updateTitle;
        //            return this;
        //        }
        //
        //        public Builder updateContentTitle(int updateContentTitle) {
        //            this.updateContentTitle = updateContentTitle;
        //            return this;
        //        }
        //
        //        public Builder updateText(int updateTextResId) {
        //            this.updateText = updateTextResId;
        //            return this;
        //        }
        //
        //        public Builder updateTextCancel(int updateCancelResId) {
        //            this.updateCancelText = updateCancelResId;
        //            return this;
        //        }
        //
        //        public Builder updateColor(int updateColorResId) {
        //            this.updateColor = updateColorResId;
        //            return this;
        //        }
        //
        //
        //        public Builder updateCancelColor(int updateCancelColorResId) {
        //            this.updateCancelColor = updateCancelColorResId;
        //            return this;
        //        }
        //
        //        public Builder updateProgressColor(int updateProgressColorResId) {
        //            this.updateProgressColor = updateProgressColorResId;
        //            return this;
        //        }
        fun isSilentMode(isSilentMode: Boolean): Builder {
            this.isSilentMode = isSilentMode
            return this
        }

        fun updateResourceId(updateResourceId: Int): Builder {
            this.updateResourceId = updateResourceId
            return this
        }

        fun build(): AppUpdate {
            return AppUpdate(this)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(newVersionUrl)
        dest.writeString(newVersionCode)
        dest.writeInt(forceUpdate)
        dest.writeString(updateInfo)
        dest.writeString(fileSize)
        dest.writeString(savePath)
        dest.writeString(md5)
        dest.writeString(downBrowserUrl)
        //        dest.writeInt(this.updateTitle);
//        dest.writeInt(this.updateContentTitle);
//        dest.writeInt(this.updateText);
//        dest.writeInt(this.updateCancelText);
//        dest.writeInt(this.updateColor);
//        dest.writeInt(this.updateCancelColor);
//        dest.writeInt(this.updateProgressColor);
        dest.writeByte(if (isSlentMode) 1.toByte() else 0.toByte())
        dest.writeInt(updateResourceId)
    }

    protected constructor(`in`: Parcel) {
        newVersionUrl = `in`.readString()
        newVersionCode = `in`.readString()
        forceUpdate = `in`.readInt()
        updateInfo = `in`.readString()
        fileSize = `in`.readString()
        savePath = `in`.readString()
        md5 = `in`.readString()
        downBrowserUrl = `in`.readString()
        //        this.updateTitle = in.readInt();
//        this.updateContentTitle = in.readInt();
//        this.updateText = in.readInt();
//        this.updateCancelText = in.readInt();
//        this.updateColor = in.readInt();
//        this.updateCancelColor = in.readInt();
//        this.updateProgressColor = in.readInt();
        isSlentMode = `in`.readByte().toInt() != 0
        updateResourceId = `in`.readInt()
    }

    companion object {
        @SuppressLint("ParcelCreator")
        val CREATOR: Parcelable.Creator<AppUpdate?> = object : Parcelable.Creator<AppUpdate?> {
            override fun createFromParcel(source: Parcel): AppUpdate? {
                return AppUpdate(source)
            }

            override fun newArray(size: Int): Array<AppUpdate?> {
                return arrayOfNulls(size)
            }
        }
    }
}