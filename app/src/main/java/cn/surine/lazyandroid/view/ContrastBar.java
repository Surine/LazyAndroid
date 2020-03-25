package cn.surine.lazyandroid.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

/**
 * Intro：
 * 一个简易的对比进度条
 *
 * @author sunliwei
 * @date 2020/3/25 14:23
 */
public class ContrastBar extends View {
    private static final int NORMAL_HEIGHT = 40;
    private Paint mainPaint;
    private Paint backPaint;
    float progress = 0F;
    int barHeight;
    private int viewWidth;
    private int radius = 10;

    public ContrastBar(Context context) {
        this(context, null);
    }

    public ContrastBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ContrastBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainPaint.setTextSize(60);
        mainPaint.setColor(Color.parseColor("#868FFF"));
        backPaint.setColor(Color.parseColor("#DDDDDD"));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            width = 500;
        }
        if (getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            height = NORMAL_HEIGHT;
        }
        if (getLayoutParams().width == ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
//            width = 500;
            height = NORMAL_HEIGHT;
        }

        viewWidth = width;
        barHeight = height;
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        @SuppressLint("DrawAllocation") RectF leftRect = new RectF(0, 0, (int) (progress * viewWidth) - barHeight / 2, barHeight);
        canvas.drawRoundRect(leftRect, radius, radius, mainPaint);
        @SuppressLint("DrawAllocation") Path leftPath = new Path();
        leftPath.moveTo(leftRect.right - 10, 0);
        leftPath.lineTo(leftRect.right + barHeight, 0);
        leftPath.lineTo(leftRect.right, barHeight);
        leftPath.lineTo(leftRect.right - 10, barHeight);
        leftPath.close();
        if (progress > 0 && progress < 1) {
            canvas.drawPath(leftPath, mainPaint);
        }
        @SuppressLint("DrawAllocation") RectF rightRect = new RectF((int) (progress * viewWidth) + barHeight, 0, viewWidth, barHeight);
        canvas.drawRoundRect(rightRect, radius, radius, backPaint);
        @SuppressLint("DrawAllocation") Path rightPath = new Path();
        rightPath.moveTo(rightRect.left + 10, 0);
        rightPath.lineTo(rightRect.left, 0);
        rightPath.lineTo(rightRect.left - barHeight, barHeight);
        rightPath.lineTo(rightRect.left, barHeight);
        rightPath.lineTo(rightRect.left + 10, barHeight);
        rightPath.close();
        canvas.drawPath(rightPath, backPaint);
    }


    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        if (progress >= 0 && progress <= 1) {
            invalidate();
        }
    }

    public int getBarHeight() {
        return barHeight;
    }

    public void setBarHeight(int barHeight) {
        this.barHeight = barHeight;
        invalidate();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        invalidate();
    }

}
