package com.hard.function.tool;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.util.Log;

public class Utils {

    public static void startActivity(Context context, Class<?> cls){
        context.startActivity(new Intent(context,cls));
    }

    /**
     * 获得两点之间的直线距离
     */
    public static float getTwoPointDistance(PointF p1, PointF p2){
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2)+Math.pow(p1.y-p2.y, 2));
    }

    /**
     * 计算两点的斜率 dy/dx
     */
    public static Float getLineSlope(PointF p1, PointF p2){
        if(p2.x - p1.x == 0) return null;
        return (p2.y - p1.y) / (p2.x - p1.x);
    }

    /**
     * 获取两点之间的中间
     */
    public static PointF getMiddlePoint(PointF p1, PointF p2){
        return new PointF((p1.x+p2.x)/2.0f, (p1.y+p2.y)/2.0f);
    }

    /**
     * 获取通过圆心斜率为slope线与圆相交的点
     * 通过arctan(slope)计算出弧度θ，x=sin(θ)*r, y=cos(θ)*r
     * @param pCenter 圆心
     * @param radius    圆半径
     * @param slope     线的斜率
     * @return
     */
    public static PointF[] getIntersectionPoints(PointF pCenter, float radius, Float slope){
        PointF[] points = new PointF[2];
        float radian, offsetX, offsetY;
        if(slope != null){
            radian = (float) Math.atan(slope);
            offsetX = (float) (Math.sin(radian)*radius);
            offsetY = (float) (Math.cos(radian)*radius);
        }else {
            offsetX = radius;
            offsetY = 0;
        }
        points[0] = new PointF(pCenter.x + offsetX, pCenter.y - offsetY);
        points[1] = new PointF(pCenter.x - offsetX, pCenter.y + offsetY);

        return points;
    }

    public static void logiJerry(String strLog){
        Log.i("Jerry debug", strLog);
    }

}
