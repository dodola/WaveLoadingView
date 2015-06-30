package com.dodola.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;


public class LoadingView extends View {
    private Paint paint;
    private int itemWidth = 0;
    private int itemHeight = 0;
    private float mInterpolatedTime = 0;
    private int ITEM_COUNT = 8;

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private class WavingAnimation extends Animation {

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;
            invalidate();
        }
    }

    WavingAnimation wa;
    private int itemSpace;

    public LoadingView(Context context) {
        super(context);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true); // 设置画笔为抗锯齿
        paint.setColor(Color.BLACK); // 设置画笔颜色
        paint.setStyle(Style.FILL);
        wa = new WavingAnimation();
        wa.setDuration(600);
        wa.setInterpolator(new LinearInterpolator());
        wa.setRepeatCount(Animation.INFINITE);
        wa.setRepeatMode(Animation.RESTART);

        itemWidth = 10;
        itemHeight = 60;
        itemSpace = 10;

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.getVisibility() != View.GONE) {
            if (!wa.hasStarted()) {
                this.startAnimation(wa);
            }

            for (int i = 1; i <= ITEM_COUNT; i++) {

                final float x = Math.abs((float) Math.sin((1 - mInterpolatedTime) * (float) Math.PI + i * 0.3));
                final int baseAlpha = (0xffffffff & 0xff000000) >>> 24;
                final int imag = Math.min((int) (baseAlpha * x) + 20, 0xff);

                final int color = imag << 24 | (0xffffffff & 0xffffff);

                paint.setColor(color);
                canvas.drawRect(i * itemSpace * 2, (itemHeight - itemHeight * x + itemHeight / 3.0f) / 2.0f, i * itemSpace * 2 + itemWidth,
                        (itemHeight - itemHeight * x) / 2.0f + itemHeight * x + itemHeight / 3.0f, paint);

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(ITEM_COUNT * itemWidth * 2 + itemWidth, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) Math.floor((itemHeight + itemHeight / 3.0f * 2)), MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
