package com.hard.function.BezierLines;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.hard.function.R;
import com.hard.function.common.GridCoordinateCustomBaseView;
import com.hard.function.tool.UIUtils;
import com.hard.function.tool.Utils;

import androidx.annotation.Nullable;

/**
 * @author Jerry Lai on 2019/08/20
 */
public class BoatWaveWithBezierView extends GridCoordinateCustomBaseView {

    // 小船浪花的宽度
    private static final int BOAT_WIDTH = 200;
    // 小船浪花的高度
    private static final int BOAT_WAVE_HEIGHT = 30;
    // 波浪高度
    private static final int WAVE_HEIGHT = 45;
    // 浪花每次的偏移量
    private final static int WAVE_OFFSET = 5;

    private Path mWavePath, mBoatWavePath, mBoatPath;   //分别为海浪、载船的浪、船的路径
    private Paint mWavePaint;
    private Bitmap mBoatBitmap;     //小船图片
    private Matrix mMatrix;     //用于变化小船方向沿着波浪边缘轨迹的
    private ValueAnimator mAnimator;
    private PathMeasure mPathMeasure;   //测量小船所处位置和方向

    private boolean isInit = false;

    private int mWaveColor, mBoatColor, waveWidth, width, height, mCurBoatOffset=0, mCurWaveOffset=0;
    private float mCurValue = 0.0f; //属性动画当前值

    public BoatWaveWithBezierView(Context context) {
        super(context);
    }

    public BoatWaveWithBezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BoatWaveWithBezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mWavePath = new Path();
        mBoatPath = new Path();
        mBoatWavePath = new Path();
        mWavePaint = new Paint();
        mWavePaint.setAntiAlias(true);

        mMatrix = new Matrix();
        mPathMeasure = new PathMeasure();

        mWaveColor = ContextCompat.getColor(context, R.color.color_wave_blue);
        mBoatColor = ContextCompat.getColor(context, R.color.color_boat_blue);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        mBoatBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.boat, options);
        //mBoatBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.boat, options);

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(4000);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.addUpdateListener((ValueAnimator animation)->{
            mCurValue = (float) mAnimator.getAnimatedValue();
            mCurWaveOffset = (mCurWaveOffset + WAVE_OFFSET) % width;
            mCurBoatOffset = (mCurBoatOffset + WAVE_OFFSET/2) % width;
            postInvalidate();
        });
    }

    /**
     * 构造波浪path，是在控件的height/2水平线上h高度的波浪
     * @param path       路径
     * @param length     浪花的宽度
     * @param h     浪花的高度
     * @param isClose    是否要闭合
     * @param lengthTime 浪花长的倍数
     */
    private void initPath(Path path, int length, int h, boolean isClose, float lengthTime){
        path.moveTo(-length, height/2);        //初始控制点;
        for (int i = -length; i < width*lengthTime + length; i+= length) {
            /**
             * rQuadTo 和 quadTo的区别是quadTo的xy参数就是每个控制点独立相对画布的位置；
             * 而rQuadTo的参数是相对上一个控制点的偏值，prePoint.x+dx prePoint.y+dy
             */
            path.rQuadTo(length/4, -h, length/2, 0);
            path.rQuadTo(length/4, h, length/2, 0);
        }
        if(isClose){
            //rLineTo的参数也是相对上一个点的偏值dx dy；带r的接口都是相对参数
            path.rLineTo(0, height/2);
            path.rLineTo(-(width + length)*2, 0);
            path.close();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinateGrid(canvas);

        float length = mPathMeasure.getLength();
        /**
         * flags Specified what aspects should be returned in the matrix.
         * 这里是返回位置和切线的矩阵作为小船旋转矩阵
         */
        mPathMeasure.getMatrix(length*mCurValue, mMatrix,
                PathMeasure.POSITION_MATRIX_FLAG | PathMeasure.TANGENT_MATRIX_FLAG);
        //设置
        mMatrix.preTranslate(-mBoatBitmap.getWidth()/2, -mBoatBitmap.getHeight()*5/6);
        canvas.drawBitmap(mBoatBitmap, mMatrix, null);

        //画船的浪花, 要用.save();restore();组合使用，不然下一个画会在当前的偏移上继续偏移
        canvas.save();
        canvas.translate(-mCurBoatOffset, 0);
        mWavePaint.setColor(mBoatColor);
        canvas.drawPath(mBoatWavePath, mWavePaint);
        canvas.restore();
        //画浪花,
        canvas.save();
        canvas.translate(-mCurWaveOffset, 0);
        mWavePaint.setColor(mWaveColor);
        canvas.drawPath(mWavePath, mWavePaint);
        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!isInit){
            isInit = true;
            width = getMeasuredWidth();
            height = getMeasuredHeight();
            waveWidth = width / 3;

            initPath(mBoatWavePath, waveWidth, BOAT_WAVE_HEIGHT, true, 2);
            initPath(mWavePath, waveWidth, WAVE_HEIGHT, true, 2);
            initPath(mBoatPath, waveWidth, BOAT_WAVE_HEIGHT, false, 1);

            mPathMeasure.setPath(mBoatPath, false);
        }
    }

    public void startAnim(){
        mAnimator.start();
    }

    public void stopAnim(){
        mAnimator.cancel();
    }
}
