package com.sweetmilkcake.beauty.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class SizeImageView extends AppCompatImageView {

    private int mInitWidth = 0;
    private int mInitHeight = 0;

    public SizeImageView(Context context) {
        this(context, null);
    }

    public SizeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInitSize(int initWidth, int initHeight) {
        this.mInitWidth = initWidth;
        this.mInitHeight = initHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 如果有设置宽和高则用
        if (mInitWidth > 0 && mInitHeight > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            // 根据获取到的图片的宽高从新计算图片的高度
            float scale = (float) mInitHeight / (float) mInitWidth;
            if (width > 0) {
                height = (int) ((float) width * scale);
            }
            setMeasuredDimension(width, height);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
