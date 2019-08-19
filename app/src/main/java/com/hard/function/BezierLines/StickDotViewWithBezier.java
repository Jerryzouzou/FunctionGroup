package com.hard.function.BezierLines;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hard.function.R;
import com.hard.function.tool.UIUtils;
import com.hard.function.tool.Utils;

import androidx.annotation.Nullable;

/**
 * @author Jerry Lai on 2019/07/23
 */
public class StickDotViewWithBezier extends AppCompatTextView {

    private DragView mDragView;
    private onDragStatusListener mDragStatusListener;
    private float width, height;

    public StickDotViewWithBezier(Context context) {
        super(context);
    }

    public StickDotViewWithBezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickDotViewWithBezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /**
     * 拖拽时的椭圆
     */
    public class DragView extends View {
        private static final int STATE_INIT = 0;//默认静止状态
        private static final int STATE_DRAG = 1;//拖拽状态
        private static final int STATE_MOVE = 2;//移动状态
        private static final int STATE_DISMISS = 3;//消失状态
        private int mState;

        private Path mPath;
        private Paint mPaint;
        private PointF mDragPoint, mStickyPonit, mControlPonit; //拖曳圆的圆点、固定圆的圆点、贝塞尔曲线控制点

        private float mDragDistance;    //拖曳的距离
        private float mMaxDistance = UIUtils.dip2px(100);     //最大拖曳距离，超过时dismiss消失
        private float width, height;
        private int mStickyRadius;      //固定圆的半径
        private int mDefaultRadius = UIUtils.dip2px(10);
        private int mDragRadius = UIUtils.dip2px(15);   //拖曳圆的半径

        private Bitmap[] mBitmaps;
        private Bitmap mCacheBitmap;
        private int[] mExplodeRes = {R.mipmap.explode1,     //消失动画资源
                R.mipmap.explode2, R.mipmap.explode3,
                R.mipmap.explode4, R.mipmap.explode5
        } ;
        private int mExplodeIndex;

        public DragView(Context context) {
            this(context, null, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public DragView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        private void init() {
            mPath = new Path();
            mPaint = UIUtils.getFillPaint(Color.RED);
            mDragPoint = new PointF();
            mStickyPonit = new PointF();
            mState = STATE_INIT;
            mBitmaps = new Bitmap[mExplodeRes.length];
            for (int i = 0; i < mExplodeRes.length; i++) {
                mBitmaps[i] = BitmapFactory.decodeResource(getResources(), mExplodeRes[i]);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if(isInRange() && mState==STATE_DRAG){
                //绘制固定小圆
                canvas.drawCircle(mStickyPonit.x, mStickyPonit.y, mStickyRadius, mPaint);

                Float slope = Utils.getLineSlope(mDragPoint, mStickyPonit); //两圆心连线的斜率
                //根据斜率分别获取两圆的交点
                PointF[] stickyPoints = Utils.getIntersectionPoints(mStickyPonit, mStickyRadius, slope);
                mDragRadius = (int) (Math.min(width, height)/2);
                PointF[] dragPoints = Utils.getIntersectionPoints(mDragPoint, mDragRadius, slope);
                //两圆心的中点作为画二阶贝塞尔曲线控制点的第二点
                mControlPonit = Utils.getMiddlePoint(mDragPoint, mStickyPonit);

                mPath.reset();
                mPath.moveTo(stickyPoints[0].x, stickyPoints[0].y);
                mPath.quadTo(mControlPonit.x, mControlPonit.y, dragPoints[0].x, dragPoints[0].y);
                mPath.lineTo(dragPoints[1].x, dragPoints[1].y);
                mPath.quadTo(mControlPonit.x, mControlPonit.y, stickyPoints[1].x, stickyPoints[1].y);
                mPath.lineTo(stickyPoints[0].x, stickyPoints[0].y);
                canvas.drawPath(mPath, mPaint);
            }

            //绘制消失动画
            if(mState==STATE_DISMISS && mExplodeIndex<mExplodeRes.length){
                canvas.drawBitmap(mBitmaps[mExplodeIndex], mDragPoint.x-width/2,
                        mDragPoint.y-height/2, mPaint);
            }

            // 绘制 StickDotView 外面内容
            if(mCacheBitmap!=null && mState!=STATE_DISMISS){
                canvas.drawBitmap(mCacheBitmap, mDragPoint.x-width/2,
                        mDragPoint.y-height/2, mPaint);
            }
        }

        /**
         * 设置缓存Bitmap，即 StickDotView 的视图, 绘制时也要将StickDotView画出
         */
        public void setCacheBitmap(Bitmap cacheBitmap) {
            mCacheBitmap = cacheBitmap;
            width = mCacheBitmap.getWidth();
            height = mCacheBitmap.getHeight();
            mDefaultRadius = (int) (Math.min(width, height)/2);
        }

        /**
         * 是否在最大拖拽范围之内
         */
        private boolean isInRange(){
            return mDragDistance < mMaxDistance;
        }

        /**
         * 拖拽在限定范围外的，消失爆炸动画（属性动画）
         */
        private void startExplodeAnim(){
            ValueAnimator animator = ValueAnimator.ofInt(0, mExplodeRes.length);
            animator.setDuration(300);
            animator.addUpdateListener((ValueAnimator animation)->{
                mExplodeIndex = (int) animation.getAnimatedValue();
                invalidate();
            });
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(mDragStatusListener != null){
                        mDragStatusListener.onDismiss();
                    }
                }
            });
            animator.start();
        }

        /**
         * 拖拽在限定范围内的，红点reset的动画
         */
        private void startResetAnimator(){
            if (mState == STATE_DRAG){
                ValueAnimator animator = ValueAnimator.ofObject(new PointEvaluator(),
                        new PointF(mDragPoint.x, mDragPoint.y), new PointF(mStickyPonit.x, mStickyPonit.y));
                animator.setDuration(500);
                animator.setInterpolator(new TimeInterpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        //振荡函数
                        float factor = 0.4f;
                        return (float) (Math.pow(2, -10 * factor) * Math.sin((input - factor / 4) * (2 * Math.PI) / factor) + 1);
                    }
                });
                animator.addUpdateListener((ValueAnimator a)->{
                    PointF curPoint = (PointF) a.getAnimatedValue();
                    mDragPoint.set(curPoint);
                    invalidate();
                });
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        clearDragView();
                        if(mDragStatusListener != null){
                            mDragStatusListener.onDrag();
                        }
                    }
                });
                animator.start();
            }else {
                //还原回固定点
                mDragPoint.set(mStickyPonit);
                invalidate();
                if(mDragStatusListener != null){
                    mDragStatusListener.onRestore();
                }
            }
        }

        /**
         * 设置固定圆的圆心和半径
         */
        public void setStickyPoint(float stickyX, float stickyY, float touchX, float touchY){
            mStickyPonit.set(stickyX, stickyY);
            mDragPoint.set(touchX, touchY);
            //圆心距，就是拖曳的距离
            mDragDistance = Utils.getTwoPointDistance(mDragPoint, mStickyPonit);
            if(mDragDistance < mMaxDistance){
                //如果拖拽距离小于规定最大距离，则固定的圆应该越来越小，这样看着才符合实际
                mStickyRadius = (int)((mDefaultRadius-mDragDistance/10)<10 ? 10 : (mDefaultRadius-mDragDistance/10));
                mState = STATE_DRAG;
            }else {
                mState = STATE_INIT;
            }
        }

        /**
         * 设置拖拽的坐标位置
         */
        public void setDragViewLocation(float x, float y){
            mDragPoint.set(x, y);
            mDragDistance = Utils.getTwoPointDistance(mDragPoint, mStickyPonit);
            if(isInRange()){
                mStickyRadius = (int)((mDefaultRadius-mDragDistance/10)<10 ? 10 : (mDefaultRadius-mDragDistance/10));
            }else {
                mState = STATE_MOVE;
                if(mDragStatusListener != null){
                    mDragStatusListener.onMove();
                }
            }
            invalidate();
        }

        /**
         * 拖曳结束抬起
         */
        public void setDragUp(){
            if(mState==STATE_DRAG && isInRange()){
                startResetAnimator();
            }else if (mState == STATE_MOVE){
                if(isInRange()){
                    //拖曳到范围外又拖回来
                    startResetAnimator();
                    mState = STATE_INIT;
                }else {
                    mState = STATE_DISMISS;
                    startExplodeAnim();
                }
            }
        }

        private void clearDragView(){
            ViewGroup viewGroup = (ViewGroup) getParent();
            viewGroup.removeView(DragView.this);
            StickDotViewWithBezier.this.setVisibility(VISIBLE);
        }
    }

    public void setDragStatusListener(onDragStatusListener dragStatusListener) {
        mDragStatusListener = dragStatusListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        View rootView = getRootView();  //获取根view
        float mRawX = event.getRawX();
        float mRawY = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);   //请求父View不拦截
                int[] cLocation = new int[2];
                getLocationOnScreen(cLocation);     ////获得当前View在屏幕上的位置
                if(rootView instanceof ViewGroup){
                    if(mDragView == null){
                        mDragView = new DragView(getContext());     //开始拖曳，新建拖曳效果view
                    }
                    //设置固定圆的圆心坐标和半径
                    mDragView.setStickyPoint(cLocation[0]+width/2, cLocation[1]+height/2,
                            mRawX, mRawY);

                    //获得缓存的bitmap，滑动时直接通过drawBitmap绘制出来
                    setDrawingCacheEnabled(true);
                    Bitmap bitmap = getDrawingCache();
                    if(bitmap != null){
                        mDragView.setCacheBitmap(bitmap);
                        //将DragView添加到RootView中，这样就可以全屏滑动了
                        ((ViewGroup) rootView).removeView(mDragView);
                        ((ViewGroup) rootView).addView(mDragView);
                        setVisibility(INVISIBLE);   //自身放到mDragView中显示了
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                getParent().requestDisallowInterceptTouchEvent(true);   //请求父View不拦截
                if(mDragView != null){
                    mDragView.setDragViewLocation(mRawX, mRawY);
                }
                break;
            case MotionEvent.ACTION_UP:
                getParent().requestDisallowInterceptTouchEvent(true);
                if(mDragView != null){
                    mDragView.setDragUp();
                }
                break;
        }
        return true;
    }

    private static class PointEvaluator implements TypeEvaluator<PointF>{

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            float x = startValue.x + fraction*(endValue.x - startValue.x);
            float y = startValue.y + fraction*(endValue.y - startValue.y);
            return new PointF(x, y);
        }
    }

    public interface onDragStatusListener{
        void onDrag();      //拖曳
        void onMove();
        void onRestore();
        void onDismiss();
    }
}
