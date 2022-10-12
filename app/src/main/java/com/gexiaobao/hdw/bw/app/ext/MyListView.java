package com.gexiaobao.hdw.bw.app.ext;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.MeasureSpec.AT_MOST;

/**
 * author : huxiaowei
 * date : 2022/10/12 23:52
 * description :
 */
public class MyListView extends RecyclerView {

    public static final String TAG = MyListView.class.getSimpleName();

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        heightMeasureSpec |= AT_MOST;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
