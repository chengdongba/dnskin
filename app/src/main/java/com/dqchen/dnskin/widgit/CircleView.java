package com.dqchen.dnskin.widgit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;

import com.dqchen.dnskin.R;
import com.dqchen.skin_core.SkinSupport;
import com.dqchen.skin_core.utils.SkinResource;

public class CircleView extends View implements SkinSupport {

    private int circleColorResId;
    private Paint mPaint;

    public CircleView(Context context) {
        this(context, null, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        circleColorResId = a.getResourceId(R.styleable.CircleView_circleColor, 0);
        a.recycle();
        mPaint = new Paint();
        mPaint.setColor(getResources().getColor(circleColorResId));
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        int radius = Math.min(width, height);
        canvas.drawCircle(width, height, radius, mPaint);
    }

    @Override
    public void applySkin() {
        //更换CircleView颜色
        if (circleColorResId != 0) {
            int color = SkinResource.getInstance().getColor(circleColorResId);
            setCircleColor(color);
        }
    }

    private void setCircleColor(@ColorInt int color) {
        mPaint.setColor(color);
        invalidate();
    }
}
