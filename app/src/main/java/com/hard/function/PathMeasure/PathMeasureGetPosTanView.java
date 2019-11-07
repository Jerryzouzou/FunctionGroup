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
 * 通过PathMeasure的getPosTan获取坐标和余弦正弦值的数据计算画飞机绕着圆转
 */
public class PathMeasureGetPosTanView extends GridCoordinateCustomBaseView {

    private static final float STEP = 0.005f;

    private Bitmap mArrowBitmap;
    private Path mCirclePath;
    private Paint mCirclePaint;
    private PathMeasure mPathMeasure;
    private Matrix mMatrix;
    private ValueAnimator valueAnimator;

    private float mCurrentValue = 0;    // 当前移动值
    private float[] mPos, mTan; //PathMeasure测量过程中getPosTan返回的数据


    public PathMeasureGetPosTanView(Context context) {
        super(context);
    }

    public PathMeasureGetPosTanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PathMeasureGetPosTanView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mPos = new float[2];
        mTan = new float[2];
        mMatrix = new Matrix();
        mArrowBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.arrow);
        mCirclePaint = UIUtils.getStrokePaint(context, 2, Color.RED);
        mCirclePath = new Path();
        mCirclePath.addCircle(0, 0, 200, Path.Direction.CW);
        mPathMeasure = new PathMeasure(mCirclePath, false);

        valueAnimator = ValueAnimator.ofFloat(0, 1f);
        valueAnimator.setDuration(5000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener((ValueAnimator animator)->{
            mCurrentValue +=STEP;
            if (mCurrentValue > 1) mCurrentValue = 0;
            //mCurrentValue = (float) animator.getAnimatedValue();
            invalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinateGrid(canvas);

        canvas.translate(mWidth/2, mHeight/2);  //移至坐标系原点
        canvas.drawPath(mCirclePath, mCirclePaint);

        mPathMeasure.getPosTan(mPathMeasure.getLength()*mCurrentValue, mPos, mTan);  //获取坐标点和余弦正弦值的数据
        float degree = (float) (Math.atan2(mTan[1], mTan[0])*180/Math.PI);  //计算对应角度

        Log.i("Jerry getPosTan_1", "------------pos[0] = " + mPos[0] + "; pos[1] = " + mPos[1]);
        Log.i("Jerry getPosTan_2", "------------tan[0](cos) = " + mTan[0] + "; tan[1](sin) = " + mTan[1]);
        Log.i("Jerry getPosTan_3", "path length = " + mPathMeasure.getLength() * mCurrentValue);
        Log.i("Jerry getPosTan_4", "degree = " + degree);

        mMatrix.reset();
        //在图片中心点旋转
        mMatrix.postRotate(degree, mArrowBitmap.getWidth()/2, mArrowBitmap.getHeight()/2);
        //设置位置，要减去图片一半宽高的偏移量
        mMatrix.postTranslate(mPos[0]-mArrowBitmap.getWidth()/2,
                mPos[1]-mArrowBitmap.getHeight()/2);
        canvas.drawBitmap(mArrowBitmap, mMatrix, mCirclePaint);
        canvas.drawCircle(mPos[0], mPos[1], 3, mCirclePaint);   //pos处画个红点
    }

    public void startAnim(){
        valueAnimator.start();
    }

    public void stopAnim(){
        valueAnimator.cancel();
    }
}
