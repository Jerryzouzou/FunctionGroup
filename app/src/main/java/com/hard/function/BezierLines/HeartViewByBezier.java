package com.hard.function.BezierLines;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;

import com.hard.function.common.GridCoordinateCustomBaseView;
import com.hard.function.tool.UIUtils;

import java.util.ArrayList;

import androidx.annotation.Nullable;

/**
 * @author Jerry Lai on 2019/06/24
 * 动态 心
 */
public class HeartViewByBezier extends GridCoordinateCustomBaseView {

    private Path heartPath;
    private Paint heartPaint;
    private ValueAnimator mAnimator;

    private ArrayList<PointF> heartPointList;
    private ArrayList<PointF> circlePointList;
    private ArrayList<PointF> curPointList;

    public HeartViewByBezier(Context context) {
        super(context);
    }

    public HeartViewByBezier(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public HeartViewByBezier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        heartPaint = UIUtils.getFillPaint(Color.RED);
        heartPath = new Path();

        heartPointList = new ArrayList<>();
        heartPointList.add(new PointF(0, UIUtils.dip2px(-38)));
        heartPointList.add(new PointF(UIUtils.dip2px(50), UIUtils.dip2px(-103)));
        heartPointList.add(new PointF(UIUtils.dip2px(112), UIUtils.dip2px(-61)));
        heartPointList.add(new PointF(UIUtils.dip2px(112), UIUtils.dip2px(-12)));
        heartPointList.add(new PointF(UIUtils.dip2px(112), UIUtils.dip2px(37)));
        heartPointList.add(new PointF(UIUtils.dip2px(51), UIUtils.dip2px(90)));
        heartPointList.add(new PointF(0, UIUtils.dip2px(129)));
        heartPointList.add(new PointF(UIUtils.dip2px(-51), UIUtils.dip2px(90)));
        heartPointList.add(new PointF(UIUtils.dip2px(-112), UIUtils.dip2px(37)));
        heartPointList.add(new PointF(UIUtils.dip2px(-112), UIUtils.dip2px(-12)));
        heartPointList.add(new PointF(UIUtils.dip2px(-112), UIUtils.dip2px(-61)));
        heartPointList.add(new PointF(UIUtils.dip2px(-50), UIUtils.dip2px(-103)));

        circlePointList = new ArrayList<>();
        circlePointList.add(new PointF(0, UIUtils.dip2px(-89)));
        circlePointList.add(new PointF(UIUtils.dip2px(50), UIUtils.dip2px(-89)));
        circlePointList.add(new PointF(UIUtils.dip2px(90), UIUtils.dip2px(-49)));
        circlePointList.add(new PointF(UIUtils.dip2px(90), 0));
        circlePointList.add(new PointF(UIUtils.dip2px(90), UIUtils.dip2px(50)));
        circlePointList.add(new PointF(UIUtils.dip2px(50), UIUtils.dip2px(90)));
        circlePointList.add(new PointF(0, UIUtils.dip2px(90)));
        circlePointList.add(new PointF(UIUtils.dip2px(-49), UIUtils.dip2px(90)));
        circlePointList.add(new PointF(UIUtils.dip2px(-89), UIUtils.dip2px(50)));
        circlePointList.add(new PointF(UIUtils.dip2px(-89), 0));
        circlePointList.add(new PointF(UIUtils.dip2px(-89), UIUtils.dip2px(-49)));
        circlePointList.add(new PointF(UIUtils.dip2px(-49), UIUtils.dip2px(-89)));

        curPointList = new ArrayList<>();
        curPointList.add(new PointF(0, UIUtils.dip2px(-89)));
        curPointList.add(new PointF(UIUtils.dip2px(50), UIUtils.dip2px(-89)));
        curPointList.add(new PointF(UIUtils.dip2px(90), UIUtils.dip2px(-49)));
        curPointList.add(new PointF(UIUtils.dip2px(90), 0));
        curPointList.add(new PointF(UIUtils.dip2px(90), UIUtils.dip2px(50)));
        curPointList.add(new PointF(UIUtils.dip2px(50), UIUtils.dip2px(90)));
        curPointList.add(new PointF(0, UIUtils.dip2px(90)));
        curPointList.add(new PointF(UIUtils.dip2px(-49), UIUtils.dip2px(90)));
        curPointList.add(new PointF(UIUtils.dip2px(-89), UIUtils.dip2px(50)));
        curPointList.add(new PointF(UIUtils.dip2px(-89), 0));
        curPointList.add(new PointF(UIUtils.dip2px(-89), UIUtils.dip2px(-49)));
        curPointList.add(new PointF(UIUtils.dip2px(-49), UIUtils.dip2px(-89)));

        mAnimator = ValueAnimator.ofFloat(0, 1f);
        mAnimator.setDuration(2000);
        mAnimator.addUpdateListener((ValueAnimator animation)->{
            //弹簧公式，factor越小摆动越多
            float x = (float) animation.getAnimatedValue();
            float factor = 0.15f;
            double value = Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1;

            for (int i = 0; i < curPointList.size(); i++) {
                PointF startPonit = circlePointList.get(i);
                PointF endPoint = heartPointList.get(i);
                curPointList.get(i).x = (float) (startPonit.x + (endPoint.x-startPonit.x)*value);
                curPointList.get(i).y = (float) (startPonit.y + (endPoint.y - startPonit.y)*value);
            }
            postInvalidate();
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinateGrid(canvas);

        canvas.translate(mWidth/2, mHeight/2);
        heartPath.reset();

        for (int i = 0; i < 4; i++) {
            if(i == 0){
                heartPath.moveTo(curPointList.get(i*3).x, curPointList.get(i*3).y);
            }else {
                heartPath.lineTo(curPointList.get(i*3).x, curPointList.get(i*3).y);
            }
            int endPointIndex = (i==3 ? 0 : i*3+3);
            heartPath.cubicTo(curPointList.get(i*3+1).x, curPointList.get(i*3+1).y,
                    curPointList.get(i*3+2).x, curPointList.get(i*3+2).y,
                    curPointList.get(endPointIndex).x, curPointList.get(endPointIndex).y);
        }

        canvas.drawPath(heartPath, heartPaint);
    }

    public void start(){
        if(mAnimator==null || mAnimator.isRunning()){
            return;
        }
        mAnimator.start();
    }
}
