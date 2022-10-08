package com.gexiaobao.hdw.bw.ui.view

import android.os.CountDownTimer
import android.widget.TextView
import java.text.DecimalFormat
import kotlin.math.floor


/**
 * created by : huxiaowei
 * @date : 20221001
 * Describe :
 */
class PeterTimeCountRefresh(var tvTime: TextView, millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {

    private var finishListener: OnTimerFinishListener? = null
    private var progressListener: OnTimerProgressListener? = null

    override fun onTick(millisUntilFinished: Long) {
        progressListener?.onTimerProgress(millisUntilFinished)

    }

    override fun onFinish() {
        finishListener?.onTimerFinish()

    }

    /**
     * 设置timer走完的回调
     */
    fun setOnTimerFinishListener(finishListener: OnTimerFinishListener) {
        this.finishListener = finishListener
    }

    /**
     * 设置监听进度的
     */
    fun setOnTimerProgressListener(progressListener: OnTimerProgressListener) {
        this.progressListener = progressListener;
    }

    /**
     * Timer 执行完成的回调
     */
    interface OnTimerFinishListener {
        fun onTimerFinish()
    }

    /**
     * Timer 进度的监听
     */
    interface OnTimerProgressListener {
        fun onTimerProgress(timeLong: Long)
    }


}