package com.qzhhh.sverticalviewlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * @author qzhhh on 2020/7/30 18:17
 */
public class SpecialVerticalTextView extends View {
    private static final String TAG = "SpecialVerticalTextView";
    private String mText = "";
    private int mFontSize = 45;
    private int mViewBottom;
    private int mTextColor = Color.BLACK;
    private Paint mPaint;
    //左边1 右边0 默认右边
    private int direction = 0;

    public SpecialVerticalTextView(Context context) {
        super(context);
        iniPaint();
    }

    public SpecialVerticalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SpecialTextView);
        mText = typedArray.getString(R.styleable.SpecialTextView_stv_text);
        mFontSize = typedArray.getInt(R.styleable.SpecialTextView_stv_text_size, 45);
        mTextColor = typedArray.getInt(R.styleable.SpecialTextView_stv_text_color, Color.BLACK);
        direction = typedArray.getInt(R.styleable.SpecialTextView_stv_text_direction, 0);
        typedArray.recycle();
        iniPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mFontSize, (int) mPaint.measureText(mText));
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mFontSize, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, (int) mPaint.measureText(mText));
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mViewBottom = bottom;
    }

    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setColor(mTextColor);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mFontSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int ang;
        if (direction == 1) {
            ang = 90;
        } else {
            ang = -90;
        }
        canvas.rotate(ang);
        //因为坐标轴需要旋转 所以margin值需要单独考虑
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) getLayoutParams();
        int bottomMargin = lp.bottomMargin;
        int topMargin = lp.topMargin;
        int correctX = -mViewBottom;
        if (topMargin != 0) {
            correctX = correctX + topMargin;
        } else if (bottomMargin != 0) {
            correctX = correctX + bottomMargin;
        }
        if (direction == 1) {
            canvas.drawText(mText, 0, (float) -(mFontSize * 0.15), mPaint);
        } else {
            //基准baseline 按照字体的大小的百分之85来算
            canvas.drawText(mText, correctX, (float) (mFontSize * 0.85), mPaint);
        }
        canvas.rotate(-ang);

    }
}
