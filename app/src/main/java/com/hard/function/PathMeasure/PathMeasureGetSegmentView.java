package com.hard.function.PathMeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.hard.function.R;
import com.hard.function.common.GridCoordinateCustomBaseView;
import com.hard.function.tool.UIUtils;

import androidx.annotation.Nullable;

/**
 * @author Jerry Lai on 2019/11/07
 * 通过PathMeasure的getSegment截取path部分，实现动态变长度的加载圈
 */
public class PathMeasureGetSegmentView extends GridCoordinateCustomBaseView {

    private Path mCirclePath, mDst;
    private Paint mCirclePaint;
    private PathMeasure mPathMeasure;
    private ValueAnimator valueAnimator;

    private float mCurrentValue = 0, mlength;    // 当前移动值

    public PathMeasureGetSegmentView(Context context) {
        super(context);
    }

    public PathMeasureGetSegmentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathMeasureGetSegmentView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mCirclePaint = UIUtils.getStrokePaint(context, 5, Color.RED);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mDst = new Path();
        mCirclePath = new Path();
        mCirclePath.addCircle(0, 0, 200, Path.Direction.CCW);
        mPathMeasure = new PathMeasure(mCirclePath, true);
        mlength = mPathMeasure.getLength();

        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener((ValueAnimator animator)->{
            mCurrentValue = (float) animator.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinateGrid(canvas);
        canvas.translate(mWidth/2, mHeight/2);  //移至坐标系原点

        mDst.reset();
        // 4.4版本以及之前的版本，需要使用这行代码，否则getSegment无效果
        // 导致这个原因是 硬件加速问题导致
        mDst.lineTo(0, 0);
        float stop = mCurrentValue * mlength;
        //超过半圆时才开始缩减
        float start = (float) (stop - ((0.5 - Math.abs(mCurrentValue - 0.5))*mlength));
        mPathMeasure.getSegment(start, stop, mDst, true);

        canvas.drawPath(mDst, mCirclePaint);
    }

    public void startAnim(){
        valueAnimator.start();
    }

    public void stopAnim(){
        valueAnimator.cancel();
    }
}
